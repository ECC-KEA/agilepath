package dev.ecckea.agilepath.backend.domain.story.model

import java.time.Instant
import java.util.*

data class Story(
    val id: UUID,
    val projectId: UUID,
    val title: String,
    val description: String?,
    val status: String,
    val priority: Int,
    val comments: List<Comment>? = emptyList(),
    val tasks: List<Task>? = emptyList(),
    val createdBy: String,
    val modifiedBy: String?,
    val createdAt: Instant,
    val modifiedAt: Instant?
)