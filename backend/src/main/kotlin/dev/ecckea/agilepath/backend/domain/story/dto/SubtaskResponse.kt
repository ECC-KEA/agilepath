package dev.ecckea.agilepath.backend.domain.story.dto

import java.time.ZonedDateTime
import java.util.*

data class SubtaskResponse(
    val id: UUID,
    val taskId: UUID,
    val title: String,
    val description: String?,
    val isDone: Boolean,
    val createdBy: String,
    val modifiedBy: String?,
    val createdAt: ZonedDateTime,
    val modifiedAt: ZonedDateTime?
)
