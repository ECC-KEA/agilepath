package dev.ecckea.agilepath.backend.domain.story.model

import java.time.Instant
import java.util.*

data class Task(
    val id: UUID,
    val storyId: UUID,
    val sprintColumnId: UUID,
    val title: String,
    val description: String?,
    val estimateTshirt: TshirtEstimate?,
    val estimatePoints: PointEstimate?,
    val createdBy: String,
    val modifiedBy: String?,
    val createdAt: Instant,
    val modifiedAt: Instant?
)