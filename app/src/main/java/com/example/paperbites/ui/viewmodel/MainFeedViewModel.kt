package com.example.paperbites.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.paperbites.data.database.Entity.PaperEntity
import com.example.paperbites.data.database.Repository.PaperRepository
import com.example.paperbites.datastore.FilterSettings
import com.example.paperbites.datastore.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainFeedViewModel(
    private val paperRepository: PaperRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    /**
     * Flow of filter settings from the data store.
     */
    val filterSettings: StateFlow<FilterSettings> = userPreferencesRepository.filterSettingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FilterSettings()
        )

    private val _softMessage = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val softMessage: SharedFlow<String> = _softMessage.asSharedFlow()

    /**
     * Flow of paged papers from the repository, reacting to filter changes.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedPapers: Flow<PagingData<PaperEntity>> = filterSettings
        .flatMapLatest { settings ->
            paperRepository.getPagedPapers(settings) { message ->
                _softMessage.tryEmit(message)
            }
        }
        .cachedIn(viewModelScope)

    /**
     * Applies new search filter settings.
     */
    fun applyFilters(settings: FilterSettings) {
        viewModelScope.launch {
            userPreferencesRepository.updateFilterSettings(settings)
        }
    }

    /**
     * Resets search filters to their default values.
     */
    fun resetFilters() {
        viewModelScope.launch {
            userPreferencesRepository.resetFilterSettings()
        }
    }

    /**
     * Marks a list of papers as served (seen by the user).
     */
    fun markAsServed(ids: List<String>) {
        if (ids.isEmpty()) return
        viewModelScope.launch {
            paperRepository.markServed(ids)
        }
    }

    /**
     * Toggles the bookmark status of a paper.
     */
    fun toggleBookmark(paper: PaperEntity) {
        viewModelScope.launch {
            paperRepository.setBookmarked(paper, !paper.bookmarked)
        }
    }
}