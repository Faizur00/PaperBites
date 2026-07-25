package com.example.paperbites.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paperbites.data.database.Entity.BookmarkEntity
import com.example.paperbites.data.database.Repository.BookmarkRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookmarkViewModel(
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    val bookmarkUiState: StateFlow<List<BookmarkEntity>> = bookmarkRepository.allBookmarks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleExpansion(paperId: String, currentExpanded: Boolean) {
        viewModelScope.launch {
            bookmarkRepository.updateExpansion(paperId, !currentExpanded)
        }
    }

    fun removeBookmark(paperId: String) {
        viewModelScope.launch {
            bookmarkRepository.removeById(paperId)
        }
    }
}