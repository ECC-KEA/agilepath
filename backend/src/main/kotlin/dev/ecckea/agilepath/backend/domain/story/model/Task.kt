package dev.ecckea.agilepath.backend.domain.story.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import dev.ecckea.agilepath.backend.domain.user.model.User
import java.time.Instant
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
data class Task(
    val id: UUID,
    val storyId: UUID,
    val sprintColumnId: UUID,
    val title: String,
    val description: String?,
    val estimateTshirt: TshirtEstimate?,
    val estimatePoints: PointEstimate?,
    val comments: List<Comment>? = emptyList(),
    val subtasks: List<Subtask>? = emptyList(),
    val assignees: List<User>? = emptyList(),
    val createdBy: String,
    val modifiedBy: String?,
    val createdAt: Instant,
    val modifiedAt: Instant?
)