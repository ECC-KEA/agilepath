package dev.ecckea.agilepath.backend.domain.analytics.model

import java.util.*

data class SprintMetadata(
    val sprintId: UUID,
    val teamCapacity: Int,
    val totalTasks: Int,
    val completedTasks: Int,
    val plannedPoints: Int,
)