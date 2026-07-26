package com.example.paperbites.data.database.Repository

import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.paperbites.data.database.AppDatabase
import com.example.paperbites.data.database.Dao.PaperDao
import com.example.paperbites.data.database.Entity.BookmarkEntity
import com.example.paperbites.data.database.Entity.PaperEntity
import com.example.paperbites.datastore.FilterSettings
import com.example.paperbites.network.OpenAlexApi
import com.example.paperbites.network.PaperRemoteMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

val Context.dataStore by preferencesDataStore(name = "app_prefs")
private val SEEDED_KEY = booleanPreferencesKey("db_seeded")

private val resilientJson = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
    explicitNulls = false
}

class PaperRepository(
    private val db: AppDatabase,
    private val api: OpenAlexApi,
    private val context: Context
) {
    private val paperDao = db.paperDao()
    private val bookmarkDao = db.bookmarkDao()

    @OptIn(ExperimentalPagingApi::class)
    fun getPagedPapers(
        filters: FilterSettings,
        onSoftError: ((String) -> Unit)? = null
    ): Flow<PagingData<PaperEntity>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 8,
            initialLoadSize = 20,
            enablePlaceholders = false
        ),
        remoteMediator = PaperRemoteMediator(api, db, filters, onSoftError),
        pagingSourceFactory = { 
            val subfieldsList = filters.subfieldIds.toList()
            paperDao.pagedPapersFiltered(
                fieldName = if (filters.fieldId == "cs") "Computer Science" else filters.fieldId,
                hasSubfields = subfieldsList.isNotEmpty(),
                subfields = subfieldsList,
                fromYear = filters.fromYear,
                toYear = filters.toYear
            )
        }
    ).flow

    suspend fun seedIfNeeded() {
        // check seed status
        val alreadySeeded = context.dataStore.data.first()[SEEDED_KEY] == true
        Log.d("SEED", "Checked flag — already seeded: $alreadySeeded")
        if (alreadySeeded) return

        // seeding part
        Log.d("SEED", "Reading asset file...")
        try {
            val jsonString = context.assets.open("json/PapersData.json")
                .bufferedReader().use { it.readText() }

            Log.d("SEED", "Decoding JSON...")
            val papers = resilientJson.decodeFromString<List<PaperEntity>>(jsonString)

            Log.d("SEED", "Decoded ${papers.size} papers")

            Log.d("SEED", "Inserting into Room...")
            paperDao.insertAll(papers)
            Log.d("SEED", "Insert complete")

            context.dataStore.edit { it[SEEDED_KEY] = true }
            Log.d("SEED", "Flag written — seeding done")
        } catch (e: Exception) {
            Log.e("SEED", "Seeding failed", e)
        }
    }

    suspend fun insertAll(papers: List<PaperEntity>) = paperDao.insertAll(papers)

    suspend fun markServed(ids: List<String>, now: Long = System.currentTimeMillis()) =
        paperDao.markServed(ids, now)

    suspend fun setBookmarked(paper: PaperEntity, saved: Boolean) {
        paperDao.setBookmarked(paper.id, saved)
        if (saved) {
            bookmarkDao.add(paper.toBookmarkEntity())
        } else {
            bookmarkDao.remove(paper.id)
        }
    }
}

fun PaperEntity.toBookmarkEntity(): BookmarkEntity {
    return BookmarkEntity(
        paperId = id,
        title = title,
        authorsDisplay = authorsDisplay,
        abstract = abstract,
        venueName = venueName,
        doi = doi,
        oaUrl = null, // PaperEntity doesn't have oaUrl currently
        publicationYear = publicationYear,
        fieldName = fieldName
    )
}

