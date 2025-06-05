package dev.ecckea.agilepath.backend.domain.analytics.model

import java.time.Instant
import java.util.*

data class TaskMetrics(
    val taskId: UUID,
    val cycleTimeHours: Double,
    val wasReopened: Boolean,
    val wasAddedDuringSprint: Boolean,
    val wasRemovedDuringSprint: Boolean,
    val wasReassigned: Boolean,
    val commentCount: Int,
    val isCompleted: Boolean,
    val subtaskCount: Int = 0,
    val timeToFirstComment: Double? = null, // Hours until first comment
    val lastActivityTime: Instant? = null,
    val assignmentChanges: Int = 0,
    val statusTransitions: Int = 0
)