package com.example.paperbites.data.database.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val paperId: String,
    val bookmarkedAt: Long = System.currentTimeMillis(),
    val title: String,
    val authorsDisplay: String,
    val abstract: String,
    val venueName: String?,
    val doi: String?,
    val oaUrl: String?
)
