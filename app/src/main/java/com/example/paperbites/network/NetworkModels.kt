package com.example.paperbites.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkResponse(
    val meta: Meta,
    val results: List<OpenAlexWork>,
)

@Serializable
data class Meta(
    val count: Int,
    @SerialName("db_response_time_ms") val dbResponseTimeMs: Int,
    val page: Int,
    @SerialName("per_page") val perPage: Int,
    @SerialName("next_cursor") val nextCursor: String? = null
)

@Serializable
data class OpenAlexWork(
    val id: String,
    val doi: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("publication_year") val publicationYear: Int? = null,
    @SerialName("publication_date") val publicationDate: String? = null,
    val type: String? = null,
    @SerialName("cited_by_count") val citedByCount: Int? = null,
    @SerialName("abstract_inverted_index") val abstractInvertedIndex: Map<String, List<Int>>? = null,
    val authorships: List<Authorship> = emptyList(),
    @SerialName("primary_location") val primaryLocation: Location? = null,
    @SerialName("primary_topic") val primaryTopic: Topic? = null,
    val topics: List<Topic> = emptyList(),
    @SerialName("open_access") val openAccess: OpenAccess? = null
)

@Serializable
data class Authorship(
    @SerialName("author_position") val authorPosition: String? = null,
    val author: Author,
    val institutions: List<Institution> = emptyList()
)

@Serializable
data class Author(
    val id: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    val orcid: String? = null
)

@Serializable
data class Institution(
    val id: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    val ror: String? = null,
    @SerialName("country_code") val countryCode: String? = null,
    val type: String? = null
)

@Serializable
data class Location(
    @SerialName("is_oa") val isOa: Boolean? = null,
    @SerialName("landing_page_url") val landingPageUrl: String? = null,
    @SerialName("pdf_url") val pdfUrl: String? = null,
    val source: Source? = null,
    val version: String? = null,
    val license: String? = null
)

@Serializable
data class Source(
    val id: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("issn_l") val issnL: String? = null,
    val type: String? = null
)

@Serializable
data class OpenAccess(
    @SerialName("is_oa") val isOa: Boolean? = null,
    @SerialName("oa_status") val oaStatus: String? = null,
    @SerialName("oa_url") val oaUrl: String? = null,
    @SerialName("any_repository_has_fulltext") val anyRepositoryHasFulltext: Boolean? = null
)

@Serializable
data class Topic(
    val id: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    val score: Float? = null,
    val subfield: TopicHierarchy? = null,
    val field: TopicHierarchy? = null,
    val domain: TopicHierarchy? = null
)

@Serializable
data class TopicHierarchy(
    val id: String? = null,
    @SerialName("display_name") val displayName: String? = null
)

