package dev.ecckea.agilepath.backend.domain.analytics.dto

import java.util.*

data class SprintMetadataResponse(
    val sprintId: UUID,
    val teamCapacity: Int,
    val totalTasks: Int,
    val completedTasks: Int,
    val plannedPoints: Int
)
