package com.example.paperbites.network

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface for the OpenAlex API.
 * See: https://docs.openalex.org/
 */
interface OpenAlexApi {
    /**
     * Search for works.
     * https://docs.openalex.org/api-entities/works/search-works
     */
    @GET("works")
    suspend fun getWorks(
        @Query("search") search: String? = null,
        @Query("filter") filter: String? = null,
        @Query("sort") sort: String? = null,
        @Query("order") order: String? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("cursor") cursor: String? = null,
        @Query("page") page: Int? = null,
        @Query("mailto") mailto: String? = null
    ): WorkResponse
}

