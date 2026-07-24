package com.example.paperbites.data.model

data class Subfield(
    val id: String,
    val displayName: String
)

data class TopicField(
    val id: String,
    val displayName: String,
    val subfields: List<Subfield>
)

val availableTopicFields = listOf(
    TopicField(
        id = "cs",
        displayName = "Computer Science",
        subfields = listOf(
            Subfield("cs.ai", "Artificial Intelligence"),
            Subfield("cs.cv", "Computer Vision and Pattern Recognition"),
            Subfield("cs.se", "Software"),
            Subfield("cs.ni", "Computer Networks and Communications"),
            Subfield("cs.th", "Theoretical Computer Science"),
            Subfield("cs.hc", "Human-Computer Interaction"),
            Subfield("cs.sp", "Signal Processing"),
            Subfield("cs.is", "Information Systems"),
            Subfield("cs.ct", "Computational Theory and Mathematics"),
            Subfield("cs.ap", "Computer Science Applications"),
            Subfield("cs.ar", "Hardware and Architecture")
        )
    )
)
