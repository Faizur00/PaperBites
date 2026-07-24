package com.example.paperbites.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.paperbites.data.database.AppDatabase
import com.example.paperbites.data.database.Entity.PaperEntity
import com.example.paperbites.datastore.FilterSettings
import java.io.IOException
import retrofit2.HttpException

/**
 * RemoteMediator acts as a coordinator between the local Room database and the OpenAlex network API.
 * It detects when the user is nearing the end of the cached data and fetches more from the network.
 */
@OptIn(ExperimentalPagingApi::class)
class PaperRemoteMediator(
    private val api: OpenAlexApi,
    private val db: AppDatabase,
    private val filters: FilterSettings
) : RemoteMediator<Int, PaperEntity>() {

    override suspend fun initialize(): InitializeAction {
        // Skip initial refresh because we already have seed data in the database.
        // This prevents an immediate network call on app startup.
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PaperEntity>
    ): MediatorResult {
        return try {
            // We only trigger network fetches when appending (scrolling down).
            if (loadType != LoadType.APPEND) {
                return MediatorResult.Success(endOfPaginationReached = false)
            }

            // Threshold Check: Only fetch if we're running low on unseen papers in the DB matching current filters.
            val subfieldsList = if (filters.subfieldIds.isEmpty()) null else filters.subfieldIds.toList()
            val unseen = db.paperDao().unseenCountFiltered(
                fieldName = filters.fieldId, // We use fieldId as name for now if they match, or map it
                subfields = subfieldsList,
                fromYear = filters.fromYear,
                toYear = filters.toYear
            )
            
            if (unseen > 25) {
                return MediatorResult.Success(endOfPaginationReached = false)
            }

            // Construct OpenAlex filter string
            // Example: "has_abstract:true,primary_topic.field.display_name:Computer Science,publication_year:>=2019"
            val filterBuilder = mutableListOf("has_abstract:true")
            
            // Map fieldId to display name if needed. 
            // In our Topic.kt, "cs" -> "Computer Science".
            // For simplicity, let's assume we can map them or they are already display names.
            // Actually, fieldId in FilterSettings is "cs".
            val fieldDisplayName = if (filters.fieldId == "cs") "Computer Science" else filters.fieldId
            filterBuilder.add("primary_topic.field.display_name:$fieldDisplayName")

            if (filters.subfieldIds.isNotEmpty()) {
                // We need to map subfield IDs back to display names for the API if we store IDs.
                // Our Topic.kt has IDs like "cs.ai" and display names "Artificial Intelligence".
                // I'll create a helper to map them or just use the display names in FilterSettings.
                // For now, let's assume we use display names in subfieldIds for matching.
                val subfieldNames = filters.subfieldIds.joinToString("|")
                filterBuilder.add("primary_topic.subfield.display_name:$subfieldNames")
            }

            filterBuilder.add("publication_year:>=${filters.fromYear}")
            filterBuilder.add("publication_year:<=${filters.toYear}")

            val filterString = filterBuilder.joinToString(",")

            // Fetch a new batch of works from OpenAlex.
            val response = api.getWorks(
                perPage = 50,
                filter = filterString
            )

            // Map network models to database entities.
            val entities = response.results.map { it.toPaperEntity() }

            // Save to database. Room handles duplicates via IGNORE strategy in the Dao.
            db.withTransaction {
                db.paperDao().insertAll(entities)
            }

            // Success! Signal if we've reached the absolute end of the API's data.
            MediatorResult.Success(endOfPaginationReached = response.results.isEmpty())
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}

