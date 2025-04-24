package dev.ecckea.agilepath.backend.domain.story.dto

import java.util.*

data class StoryRequest(
    val projectId: UUID,
    val title: String,
    val description: String?,
    val status: String,
    val priority: Int
)
