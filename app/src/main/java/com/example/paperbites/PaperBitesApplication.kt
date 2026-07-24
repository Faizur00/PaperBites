package com.example.paperbites

import android.app.Application
import com.example.paperbites.data.AppContainer
import com.example.paperbites.data.DefaultAppContainer

class PaperBitesApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}

