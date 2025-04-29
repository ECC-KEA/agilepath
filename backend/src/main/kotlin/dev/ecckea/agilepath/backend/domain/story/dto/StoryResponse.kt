package dev.ecckea.agilepath.backend.domain.story.dto

import java.time.ZonedDateTime
import java.util.*

data class StoryResponse(
    val id: UUID,
    val projectId: UUID,
    val title: String,
    val description: String?,
    val status: String,
    val priority: Int,
    val comments: List<CommentResponse>? = emptyList(),
    val tasks: List<TaskResponse>? = emptyList(),
    val createdBy: String,
    val modifiedBy: String?,
    val createdAt: ZonedDateTime,
    val modifiedAt: ZonedDateTime?
)
