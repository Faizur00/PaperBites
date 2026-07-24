package com.example.paperbites.network

import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

/**
 * Singleton object that provides the Retrofit API service.
 */
object NetworkModule {
    private const val BASE_URL = "https://api.openalex.org/"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val openAlexApi: OpenAlexApi by lazy {
        retrofit.create(OpenAlexApi::class.java)
    }
}

