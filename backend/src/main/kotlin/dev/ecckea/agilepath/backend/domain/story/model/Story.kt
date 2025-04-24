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
    val createdBy: String,
    val modifiedBy: String?,
    val createdAt: Instant,
    val modifiedAt: Instant?
)