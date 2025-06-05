package dev.ecckea.agilepath.backend.domain.analytics.model

data class TaskComplexityBreakdown(
    val simple: Int,      // <= 1 subtask, <= 2 comments
    val moderate: Int,    // 2-4 subtasks OR 3-6 comments
    val complex: Int      // > 4 subtasks OR > 6 comments
)