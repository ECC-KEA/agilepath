package dev.ecckea.agilepath.backend.domain.story.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.Instant
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
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