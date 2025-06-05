package dev.ecckea.agilepath.backend.domain.analytics.model

import java.util.*

data class SprintMetrics(
    val sprintId: UUID,

    // Core sprint performance
    val velocity: Int,                           // Story points completed
    val durationDays: Int,                       // Actual sprint duration
    val completionRate: Int,                     // Percentage of tasks completed (0-100)

    // Cycle time analysis
    val avgCycleTimeHours: Double,               // Average time from start to completion
    val longestCycleTimeTaskId: UUID?,           // Task that took longest to complete

    // Quality and stability indicators
    val numReopenedTasks: Int,                   // Tasks that were reopened
    val numAddedTasksDuringSprint: Int,          // Scope changes - additions
    val numRemovedTasksDuringSprint: Int,        // Scope changes - removals
    val numReassignedTasks: Int,                 // Assignment instability

    // Collaboration metrics
    val numTasksWithComments: Int,               // Tasks with active discussion
    val avgTaskCommentCount: Double,             // Average comments per task
    val tasksWithoutComments: Int,               // Tasks with no collaboration
    val collaborationScore: Double,              // 0-100 collaboration health score

    // Task complexity and structure
    val avgSubtaskCount: Double,                 // Average subtasks per task
    val tasksByComplexity: TaskComplexityBreakdown, // Simple/moderate/complex breakdown

    // Activity and workflow metrics
    val totalAssignmentChanges: Int,             // All assignment-related events
    val totalStatusTransitions: Int              // All status change events
)