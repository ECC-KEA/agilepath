package dev.ecckea.agilepath.backend.domain.story.dto

import dev.ecckea.agilepath.backend.domain.story.model.PointEstimate
import dev.ecckea.agilepath.backend.domain.story.model.TshirtEstimate
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
    val createdBy: String,
    val modifiedBy: String?,
    val createdAt: ZonedDateTime,
    val modifiedAt: ZonedDateTime?
)
