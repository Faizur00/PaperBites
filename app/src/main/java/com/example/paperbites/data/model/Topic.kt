package com.example.paperbites.data.model

data class Subfield(
    val id: String,
    val displayName: String,
    val openAlexId: String
)

data class TopicField(
    val id: String,
    val displayName: String,
    val openAlexId: String,
    val subfields: List<Subfield>
)

val availableTopicFields = listOf(
    TopicField(
        id = "cs",
        displayName = "Computer Science",
        openAlexId = "17",
        subfields = listOf(
            Subfield("cs.ai", "Artificial Intelligence", "1702"),
            Subfield("cs.cv", "Computer Vision and Pattern Recognition", "1707"),
            Subfield("cs.se", "Software", "1712"),
            Subfield("cs.ni", "Computer Networks and Communications", "1705"),
            Subfield("cs.th", "Theoretical Computer Science", "2614"),
            Subfield("cs.hc", "Human-Computer Interaction", "1709"),
            Subfield("cs.sp", "Signal Processing", "1711"),
            Subfield("cs.is", "Information Systems", "1710"),
            Subfield("cs.ct", "Computational Theory and Mathematics", "1703"),
            Subfield("cs.ap", "Computer Science Applications", "1706"),
            Subfield("cs.ar", "Hardware and Architecture", "1708")
        )
    )
)
