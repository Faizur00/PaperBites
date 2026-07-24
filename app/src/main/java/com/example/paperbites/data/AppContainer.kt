package com.example.paperbites.data

import android.content.Context
import com.example.paperbites.data.database.AppDatabase
import com.example.paperbites.data.database.Repository.PaperRepository
import com.example.paperbites.datastore.UserPreferencesRepository
import com.example.paperbites.network.NetworkModule
import com.example.paperbites.network.OpenAlexApi

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val paperRepository: PaperRepository
    val userPreferencesRepository: UserPreferencesRepository
}

/**
 * [AppContainer] implementation that provides instance of [PaperRepository]
 */
class DefaultAppContainer(private val context: Context) : AppContainer {

    private val db: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    private val api: OpenAlexApi by lazy {
        NetworkModule.openAlexApi
    }

    override val paperRepository: PaperRepository by lazy {
        PaperRepository(db, api, context)
    }

    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context)
    }
}

