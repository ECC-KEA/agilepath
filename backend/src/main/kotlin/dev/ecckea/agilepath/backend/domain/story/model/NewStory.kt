package dev.ecckea.agilepath.backend.domain.story.model

import java.time.Instant
import java.util.*

data class NewStory(
    val projectId: UUID,
    val title: String,
    val description: String?,
    val acceptanceCriteria: String?,
    val status: String,
    val priority: Int,
    val createdBy: String,
    val createdAt: Instant,
)
