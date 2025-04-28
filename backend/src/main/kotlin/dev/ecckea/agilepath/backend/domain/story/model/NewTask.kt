package dev.ecckea.agilepath.backend.domain.story.model

import java.time.Instant
import java.util.*

data class NewTask(
    val storyId: UUID,
    val sprintColumnId: UUID,
    val title: String,
    val description: String?,
    val estimateTshirt: TshirtEstimate?,
    val estimatePoints: PointEstimate?,
    val assigneeIds: List<String>,
    val createdBy: String,
    val createdAt: Instant,
)
