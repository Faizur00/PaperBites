package com.example.paperbites.data.database.Dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.paperbites.data.database.Entity.PaperEntity

@Dao
interface PaperDao {
    // Inserts a list of papers into the database, ignoring any that already exist
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(papers: List<PaperEntity>)

    // filtered feed with full search tunings
    @Query("""
        SELECT * FROM papers 
        WHERE (served = 0 OR sessionId = :sessionId)
        AND (:fieldName IS NULL OR fieldName = :fieldName)
        AND (:hasSubfields = 0 OR subfield IN (:subfields))
        AND (:fromYear IS NULL OR publicationYear >= :fromYear)
        AND (:toYear IS NULL OR publicationYear <= :toYear)
        ORDER BY shuffleKey ASC
    """)
    fun pagedPapersFiltered(
        fieldName: String?,
        hasSubfields: Boolean,
        subfields: List<String>,
        fromYear: Int?,
        toYear: Int?,
        sessionId: String?
    ): PagingSource<Int, PaperEntity>

    // Returns the total number of papers that haven't been seen by the user yet,
    // matching the current filters.
    @Query("""
        SELECT COUNT(*) FROM papers 
        WHERE served = 0 
        AND (:fieldName IS NULL OR fieldName = :fieldName)
        AND (:hasSubfields = 0 OR subfield IN (:subfields))
        AND (:fromYear IS NULL OR publicationYear >= :fromYear)
        AND (:toYear IS NULL OR publicationYear <= :toYear)
    """)
    suspend fun unseenCountFiltered(
        fieldName: String?,
        hasSubfields: Boolean,
        subfields: List<String>,
        fromYear: Int?,
        toYear: Int?
    ): Int

    // Resets served status back to 0 (unseen) for papers matching the specified filter criteria.
    @Query("""
        UPDATE papers 
        SET served = 0, servedAt = NULL
        WHERE (:fieldName IS NULL OR fieldName = :fieldName)
        AND (:hasSubfields = 0 OR subfield IN (:subfields))
        AND (:fromYear IS NULL OR publicationYear >= :fromYear)
        AND (:toYear IS NULL OR publicationYear <= :toYear)
    """)
    suspend fun resetServedFiltered(
        fieldName: String?,
        hasSubfields: Boolean,
        subfields: List<String>,
        fromYear: Int?,
        toYear: Int?
    ): Int

    @Query("DELETE FROM papers WHERE served = 1 AND bookmarked = 0 AND sessionId != :currentSessionId")
    suspend fun clearOldServed(currentSessionId: String)

    // filtered feed — backs a chosen fieldName tab/chip
    @Query("SELECT * FROM papers WHERE fieldName = :field ORDER BY shuffleKey ASC")
    fun pagingSourceByField(field: String): PagingSource<Int, PaperEntity>

    // Returns the number of unseen papers specifically for a given field/category
    @Query("SELECT COUNT(*) FROM papers WHERE served = 0 AND fieldName = :field")
    suspend fun unseenCountByField(field: String): Int

    // filtered feed — backs a chosen language
    @Query("SELECT * FROM papers WHERE language = :language ORDER BY shuffleKey ASC")
    fun pagingSourceByLanguage(language: String): PagingSource<Int, PaperEntity>

    // Returns the number of unseen papers specifically for a given language
    @Query("SELECT COUNT(*) FROM papers WHERE served = 0 AND language = :language")
    suspend fun unseenCountByLanguage(language: String): Int

    // Marks a list of papers as "served" (seen) and records the timestamp
    @Query("UPDATE papers SET served = 1, servedAt = :now WHERE id IN (:ids)")
    suspend fun markServed(ids: List<String>, now: Long = System.currentTimeMillis())

    // Updates the bookmark status for a specific paper
    @Query("UPDATE papers SET bookmarked = :saved WHERE id = :id")
    suspend fun setBookmarked(id: String, saved: Boolean)

    // Updates the shuffleKey for a specific paper
    @Query("UPDATE papers SET shuffleKey = :shuffleKey WHERE id = :id")
    suspend fun updateShuffleKey(id: String, shuffleKey: Double)
}
