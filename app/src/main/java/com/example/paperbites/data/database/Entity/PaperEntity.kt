package com.example.paperbites.data.database.Entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "papers",
    indices = [
        Index(value = ["served", "shuffleKey"]),
        Index(value = ["served", "fieldName", "shuffleKey"]),
        Index(value = ["bookmarked"]),
        Index(value = ["language"]),
        Index(value = ["sessionId"])
    ]
)
@Serializable
data class PaperEntity(
    @PrimaryKey val id: String,
    val doi: String?,
    val title: String,
    val abstract: String,
    val authorsDisplay: String,
    val venueName: String?,
    val publicationYear: Int?,
    val domainName: String?,
    val fieldName: String?,
    val primaryTopicName: String?,
    val subfield: String? = null,
    val language: String? = null,
    val shuffleKey: Double = Math.random(),
    val served: Boolean = false,
    val servedAt: Long? = null,
    val bookmarked: Boolean = false,
    val sessionId: String? = null
)
