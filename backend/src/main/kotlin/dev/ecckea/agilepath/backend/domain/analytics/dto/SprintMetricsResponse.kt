package dev.ecckea.agilepath.backend.domain.analytics.dto

import java.util.*

data class SprintMetricsResponse(
    val sprintId: UUID,
    val velocity: Int,
    val durationDays: Int,
    val completionRate: Int,
    val avgCycleTimeHours: Double,
    val longestCycleTimeTaskId: UUID?,
    val numReopenedTasks: Int,
    val numAddedTasksDuringSprint: Int,
    val numRemovedTasksDuringSprint: Int,
    val numReassignedTasks: Int,
    val numTasksWithComments: Int,
    val avgTaskCommentCount: Double,
    val tasksWithoutComments: Int,
    val collaborationScore: Double,
    val avgSubtaskCount: Double,
    val tasksByComplexity: TaskComplexityResponse,
    val totalAssignmentChanges: Int,
    val totalStatusTransitions: Int
)
