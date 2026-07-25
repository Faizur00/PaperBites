package com.example.paperbites.data.database.Dao

import androidx.room.*
import com.example.paperbites.data.database.Entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    // Save a new bookmark
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(bookmark: BookmarkEntity)

    // Delete a bookmark using its ID
    @Query("DELETE FROM bookmarks WHERE paperId = :id")
    suspend fun remove(id: String)

    // Get all bookmarks sorted by newest first
    @Query("SELECT * FROM bookmarks ORDER BY bookmarkedAt DESC")
    fun observeAll(): Flow<List<BookmarkEntity>>

    @Query("UPDATE bookmarks SET isExpanded = :isExpanded WHERE paperId = :id")
    suspend fun updateExpansion(id: String, isExpanded: Boolean)
}
