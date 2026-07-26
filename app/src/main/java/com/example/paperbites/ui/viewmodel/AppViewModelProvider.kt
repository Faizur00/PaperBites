package com.example.paperbites.ui.viewmodel

import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.paperbites.PaperBitesApplication

/**
 * Provides Factory to create instance of ViewModel for the entire PaperBytes app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for MainFeedViewModel
        initializer {
            val app = this.PaperBitesApplication()
            MainFeedViewModel(
                app.container.paperRepository,
                app.container.userPreferencesRepository
            )
        }

        // Initializer for BookmarkViewModel
        initializer {
            val app = this.PaperBitesApplication()
            BookmarkViewModel(
                app.container.bookmarkRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [PaperBitesApplication].
 */
fun CreationExtras.PaperBitesApplication(): PaperBitesApplication =
    (this[androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PaperBitesApplication)

