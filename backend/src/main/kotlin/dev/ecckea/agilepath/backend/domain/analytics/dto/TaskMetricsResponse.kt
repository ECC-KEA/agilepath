package dev.ecckea.agilepath.backend.domain.analytics.dto

import java.time.Instant
import java.util.*

data class TaskMetricsResponse(
    val taskId: UUID,
    val cycleTimeHours: Double,
    val wasReopened: Boolean,
    val wasAddedDuringSprint: Boolean,
    val wasRemovedDuringSprint: Boolean,
    val wasReassigned: Boolean,
    val commentCount: Int,
    val isCompleted: Boolean,
    val subtaskCount: Int,
    val timeToFirstComment: Double?,
    val lastActivityTime: Instant?,
    val assignmentChanges: Int,
    val statusTransitions: Int
)
