package com.example.paperbites.network

import com.example.paperbites.data.database.Entity.PaperEntity

/**
 * Extension function to map OpenAlexWork (network model) to PaperEntity (database model).
 */
fun OpenAlexWork.toPaperEntity(
    shuffleKey: Double = Math.random(),
    sessionId: String? = null
): PaperEntity {
    return PaperEntity(
        id = this.id,
        doi = this.doi,
        title = this.displayName ?: "Untitled",
        abstract = reconstructAbstract(this.abstractInvertedIndex),
        authorsDisplay = this.authorships.joinToString(", ") { it.author.displayName ?: "Unknown Author" },
        venueName = this.primaryLocation?.source?.displayName,
        publicationYear = this.publicationYear,
        domainName = this.primaryTopic?.domain?.displayName,
        fieldName = this.primaryTopic?.field?.displayName,
        subfield = this.primaryTopic?.subfield?.displayName,
        primaryTopicName = this.primaryTopic?.displayName,
        language = null, // OpenAlex language data is usually in a different field if needed
        shuffleKey = shuffleKey,
        sessionId = sessionId
    )
}

/**
 * Reconstructs the abstract from the inverted index format provided by OpenAlex.
 * OpenAlex provides abstracts as a map where keys are words and values are lists of 
 * their positions in the original text.
 */
private fun reconstructAbstract(invertedIndex: Map<String, List<Int>>?): String {
    if (invertedIndex.isNullOrEmpty()) return ""
    
    // Create a list of pairs (position, word)
    val wordPositions = mutableListOf<Pair<Int, String>>()
    for ((word, positions) in invertedIndex) {
        for (pos in positions) {
            wordPositions.add(pos to word)
        }
    }
    
    // Sort by position and join the words
    return wordPositions.sortedBy { it.first }
        .joinToString(" ") { it.second }
}

