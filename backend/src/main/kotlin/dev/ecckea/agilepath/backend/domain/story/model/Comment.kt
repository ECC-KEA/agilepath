package dev.ecckea.agilepath.backend.domain.story.model

import java.time.Instant
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonTypeName("dev.ecckea.agilepath.backend.domain.story.model.Comment")
data class Comment(
    val id: UUID,
    val content: String,
    val storyId: UUID?,
    val taskId: UUID?,
    val createdBy: String,
    val modifiedBy: String?,
    val createdAt: Instant,
    val modifiedAt: Instant?
)