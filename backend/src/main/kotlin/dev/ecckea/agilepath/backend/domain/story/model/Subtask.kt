package dev.ecckea.agilepath.backend.domain.story.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.Instant
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
data class Subtask(
    val id: UUID,
    val taskId: UUID,
    val title: String,
    val description: String?,
    val isDone: Boolean,
    val createdBy: String,
    val modifiedBy: String?,
    val createdAt: Instant,
    val modifiedAt: Instant?
)