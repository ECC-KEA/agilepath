package dev.ecckea.agilepath.backend.domain.analytics.model

data class TaskInsights(
    val totalTasks: Int,
    val completedTasks: Int,
    val reassignedTasks: Int,
    val avgCycleTimeHours: Double,
    val medianCycleTimeHours: Double,
    val cycleTimeStdDev: Double, // Standard deviation of cycle times
    val tasksWithComments: Int,
    val avgCommentCount: Double,
    val avgAssigneeCount: Double,
    val avgSubtaskCount: Double,
)