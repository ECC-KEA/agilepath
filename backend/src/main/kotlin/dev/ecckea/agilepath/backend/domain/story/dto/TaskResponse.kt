package dev.ecckea.agilepath.backend.domain.story.dto

import dev.ecckea.agilepath.backend.domain.story.model.PointEstimate
import dev.ecckea.agilepath.backend.domain.story.model.TshirtEstimate
import dev.ecckea.agilepath.backend.domain.user.dto.UserResponse
import java.time.ZonedDateTime
import java.util.*

data class TaskResponse(
    val id: UUID,
    val storyId: UUID,
    val sprintColumnId: UUID,
    val title: String,
    val description: String?,
    val estimateTshirt: TshirtEstimate?,
    val estimatePoints: PointEstimate?,
    val comments: List<CommentResponse>? = emptyList(),
    val subtasks: List<SubtaskResponse>? = emptyList(),
    val assignees: List<UserResponse>? = emptyList(),
    val createdBy: String,
    val modifiedBy: String?,
    val createdAt: ZonedDateTime,
    val modifiedAt: ZonedDateTime?
)
