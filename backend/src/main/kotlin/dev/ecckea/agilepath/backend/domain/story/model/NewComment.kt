package dev.ecckea.agilepath.backend.domain.story.model

import java.time.Instant
import java.util.*

data class NewComment(
    val content: String,
    val storyId: UUID?,
    val taskId: UUID?,
    val createdBy: String,
    val createdAt: Instant,
)
