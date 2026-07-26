package com.example.paperbites.utils

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

fun readFullPaper(context: Context, doi: String? = null) {
    if (doi.isNullOrBlank()) {
        return
        // TODO: return something that can be used on the UI layer later
    }

    val fullUrl = "https://doi.org/$doi"
    val customTabIntent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()

    customTabIntent.launchUrl(context, fullUrl.toUri())
}
