package dev.ecckea.agilepath.backend.domain.story.dto

import java.time.ZonedDateTime
import java.util.*

data class CommentResponse(
    val id: UUID,
    val content: String,
    val storyId: UUID?,
    val taskId: UUID?,
    val createdBy: String,
    val modifiedBy: String?,
    val createdAt: ZonedDateTime,
    val modifiedAt: ZonedDateTime?
)