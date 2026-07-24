package com.example.paperbites.data.database.Repository

import com.example.paperbites.data.database.Dao.BookmarkDao
import com.example.paperbites.data.database.Entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

class BookmarkRepository(
    private val bookmarkDao: BookmarkDao
) {
    val allBookmarks: Flow<List<BookmarkEntity>> = bookmarkDao.observeAll()

    suspend fun insert(bookmark: BookmarkEntity) {
        bookmarkDao.add(bookmark)
    }

    suspend fun delete(bookmark: BookmarkEntity) {
        bookmarkDao.remove(id = bookmark.paperId)
    }
}
