package com.example.paperbites.network

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.paperbites.data.database.AppDatabase
import com.example.paperbites.data.database.Entity.PaperEntity
import com.example.paperbites.data.database.Entity.RemoteKeys
import com.example.paperbites.data.model.availableTopicFields
import com.example.paperbites.datastore.FilterSettings
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
    private val sessionId: String,
    private val onSoftError: ((String) -> Unit)? = null
) : RemoteMediator<Int, PaperEntity>() {

    private val paperDao = db.paperDao()
    private val remoteKeysDao = db.remoteKeysDao()

    private fun getFilterId(): String {
        return "field=${filters.fieldId};subfields=${filters.subfieldIds.sorted().joinToString(",")};years=${filters.fromYear}-${filters.toYear}"
    }

    override suspend fun initialize(): InitializeAction {
        // TikTok-like: always trigger initial refresh to get fresh content immediately
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
        
        val subfieldOpenAlexIds = subfieldsList.mapNotNull { name ->
            topicField.subfields.find { it.displayName == name }?.openAlexId
        }

        val filterId = getFilterId()

        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKeys = remoteKeysDao.remoteKeysByFilter(filterId)
                    if (remoteKeys?.nextPage == null) {
                        return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    }
                    remoteKeys.nextPage
                }
            }

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

            // Fetch from network using sample for REFRESH (TikTok-like)
            val response = try {
                if (loadType == LoadType.REFRESH) {
                    api.getWorks(
                        sample = state.config.pageSize,
                        filter = filterString
                    )
                } else {
                    api.getWorks(
                        perPage = state.config.pageSize,
                        page = page,
                        filter = filterString
                    )
                }
            } catch (e: Exception) {
                Log.e("PaperRemoteMediator", "Fetch failed: ${e.message}", e)
                if (loadType == LoadType.REFRESH) {
                    val count = paperDao.unseenCountFiltered(
                        fieldName = fieldDisplayName,
                        hasSubfields = hasSubfields,
                        subfields = subfieldsList,
                        fromYear = filters.fromYear,
                        toYear = filters.toYear
                    )
                    if (count > 0) {
                        onSoftError?.invoke("Showing offline content")
                        return MediatorResult.Success(endOfPaginationReached = false)
                    }
                }
                return MediatorResult.Error(e)
            }

            val endOfPaginationReached = response.results.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    // Cleanup old session papers that aren't bookmarked
                    paperDao.clearOldServed(sessionId)
                    remoteKeysDao.deleteByFilter(filterId)
                }

                val nextPage = if (endOfPaginationReached) null else page + 1
                remoteKeysDao.insertOrReplace(RemoteKeys(filterId, nextPage))

                val bias = if (loadType == LoadType.REFRESH) -1.0 * System.currentTimeMillis() else page.toDouble()
                
                val entities = response.results.mapIndexed { index, work ->
                    work.toPaperEntity(
                        shuffleKey = bias + (index.toDouble() / 1000.0) + Math.random(),
                        sessionId = sessionId
                    ).copy(sessionId = sessionId) // Ensure sessionId is set
                }

                paperDao.insertAll(entities)
                if (loadType == LoadType.REFRESH) {
                    entities.forEach { paper ->
                        paperDao.updateShuffleKey(paper.id, paper.shuffleKey)
                    }
                }
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
