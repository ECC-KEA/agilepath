package dev.ecckea.agilepath.backend.domain.analytics.model

import java.util.*

data class TaskMetadata(
    val taskId: UUID,
    val estimatePoints: Int?,
    val estimateTshirt: String?, // raw value or converted name
    val subtaskCount: Int,
    val commentCount: Int,
    val assigneeCount: Int
)
