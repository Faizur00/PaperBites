package com.example.paperbites.ui.viewmodel

import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.paperbites.PaperBitesApplication
import com.example.paperbites.ui.mainfeed.MainFeedViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire PaperBytes app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for MainFeedViewModel
        initializer {
            val app = PaperBitesApplication()
            MainFeedViewModel(
                app.container.paperRepository,
                app.container.userPreferencesRepository
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

