package dev.ecckea.agilepath.backend.domain.analytics.model

data class SprintComparison(
    val current: SprintMetrics,
    val previous: SprintMetrics,
    val velocityChange: Int,
    val completionRateChange: Int,
    val avgCycleTimeChange: Double,
    val collaborationScoreChange: Double
)
