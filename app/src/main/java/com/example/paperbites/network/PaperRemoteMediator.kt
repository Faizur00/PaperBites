package com.example.paperbites.network

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.paperbites.data.database.AppDatabase
import com.example.paperbites.data.database.Entity.PaperEntity
import com.example.paperbites.data.model.availableTopicFields
import com.example.paperbites.datastore.FilterSettings
import java.io.IOException
import retrofit2.HttpException

/**
 * RemoteMediator acts as a coordinator between the local Room database and the OpenAlex network API.
 * It handles both hard REFRESH (full reset + biased fetch) and APPEND (passive top-off).
 */
@OptIn(ExperimentalPagingApi::class)
class PaperRemoteMediator(
    private val api: OpenAlexApi,
    private val db: AppDatabase,
    private val filters: FilterSettings,
    private val onSoftError: ((String) -> Unit)? = null
) : RemoteMediator<Int, PaperEntity>() {

    override suspend fun initialize(): InitializeAction {
        // Trigger initial refresh on load to ensure initial feed reset + fetch
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PaperEntity>
    ): MediatorResult {
        val topicField = availableTopicFields.find { it.id == filters.fieldId } ?: availableTopicFields.first()
        val fieldDisplayName = topicField.displayName
        val fieldOpenAlexId = topicField.openAlexId
        
        val subfieldsList = filters.subfieldIds.toList()
        val hasSubfields = subfieldsList.isNotEmpty()
        
        // Map subfield display names to OpenAlex IDs
        val subfieldOpenAlexIds = subfieldsList.mapNotNull { name ->
            topicField.subfields.find { it.displayName == name }?.openAlexId
        }

        return try {
            when (loadType) {
                LoadType.REFRESH -> {
                    // 1. Reset first, unconditionally as its own write, committed BEFORE network attempt.
                    db.paperDao().resetServedFiltered(
                        fieldName = fieldDisplayName,
                        hasSubfields = hasSubfields,
                        subfields = subfieldsList,
                        fromYear = filters.fromYear,
                        toYear = filters.toYear
                    )

                    val resetCount = db.paperDao().unseenCountFiltered(
                        fieldName = fieldDisplayName,
                        hasSubfields = hasSubfields,
                        subfields = subfieldsList,
                        fromYear = filters.fromYear,
                        toYear = filters.toYear
                    )

                    // Construct OpenAlex filter string
                    val filterBuilder = mutableListOf("has_abstract:true")
                    filterBuilder.add("primary_topic.field.id:$fieldOpenAlexId")
                    
                    if (subfieldOpenAlexIds.isNotEmpty()) {
                        val idsString = subfieldOpenAlexIds.joinToString("|")
                        filterBuilder.add("primary_topic.subfield.id:$idsString")
                    }
                    if (filters.fromYear != null || filters.toYear != null) {
                        val from = filters.fromYear ?: ""
                        val to = filters.toYear ?: ""
                        filterBuilder.add("publication_year:$from-$to")
                    }

                    val filterString = filterBuilder.joinToString(",")

                    // 2. Fetch fresh batch, best-effort as a separate step
                    try {
                        val response = api.getWorks(
                            perPage = 20,
                            filter = filterString
                        )

                        // Biased shuffleKey range reserved specifically for refresh-fetched rows
                        // (negative timestamp base guarantees sorting before any reset rows)
                        val refreshBiasBase = -1.0 * System.currentTimeMillis()
                        val entities = response.results.map { work ->
                            work.toPaperEntity(shuffleKey = refreshBiasBase + Math.random())
                        }

                        db.withTransaction {
                            db.paperDao().insertAll(entities)
                            // For REFRESH, ensure even existing papers move to the top
                            entities.forEach { paper ->
                                db.paperDao().updateShuffleKey(paper.id, paper.shuffleKey)
                            }
                        }

                        MediatorResult.Success(endOfPaginationReached = response.results.isEmpty())
                    } catch (e: Exception) {
                        Log.e("PaperRemoteMediator", "Fetch failed: ${e.message}", e)
                        if (e is IOException || e is HttpException) {
                            if (resetCount > 0) {
                                // Soft, non-blocking signal: reset gave the user existing items to scroll through
                                onSoftError?.invoke("Couldn't get new content, showing existing")
                                MediatorResult.Success(endOfPaginationReached = false)
                            } else {
                                // Brand-new filter with zero cached rows, offline: unrecoverable
                                MediatorResult.Error(e)
                            }
                        } else {
                            throw e
                        }
                    }
                }

                LoadType.APPEND -> {
                    // Passive, threshold-gated background top-off
                    val unseen = db.paperDao().unseenCountFiltered(
                        fieldName = fieldDisplayName,
                        hasSubfields = hasSubfields,
                        subfields = subfieldsList,
                        fromYear = filters.fromYear,
                        toYear = filters.toYear
                    )

                    if (unseen > 10) {
                        return MediatorResult.Success(endOfPaginationReached = false)
                    }

                    val filterBuilder = mutableListOf("has_abstract:true")
                    filterBuilder.add("primary_topic.field.id:$fieldOpenAlexId")
                    
                    if (subfieldOpenAlexIds.isNotEmpty()) {
                        val idsString = subfieldOpenAlexIds.joinToString("|")
                        filterBuilder.add("primary_topic.subfield.id:$idsString")
                    }
                    if (filters.fromYear != null || filters.toYear != null) {
                        val from = filters.fromYear ?: ""
                        val to = filters.toYear ?: ""
                        filterBuilder.add("publication_year:$from-$to")
                    }

                    val filterString = filterBuilder.joinToString(",")

                    val response = api.getWorks(
                        perPage = 20,
                        filter = filterString
                    )

                    val entities = response.results.map { it.toPaperEntity() }

                    db.withTransaction {
                        db.paperDao().insertAll(entities)
                    }

                    MediatorResult.Success(endOfPaginationReached = response.results.isEmpty())
                }

                LoadType.PREPEND -> {
                    MediatorResult.Success(endOfPaginationReached = true)
                }
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
