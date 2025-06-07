package dev.ecckea.agilepath.backend.domain.analytics.dto

data class SprintComparisonResponse(
    val current: SprintMetricsResponse,
    val previous: SprintMetricsResponse,
    val velocityChange: Int,
    val completionRateChange: Int,
    val avgCycleTimeChange: Double,
    val collaborationScoreChange: Double
)
