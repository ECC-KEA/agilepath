package dev.ecckea.agilepath.backend.domain.analytics.model

data class SprintTrends(
    val sprints: List<SprintMetrics>,
    val avgVelocity: Double,
    val avgCompletionRate: Double,
    val avgCycleTime: Double,
    val avgCollaborationScore: Double,
    val velocityTrend: TrendDirection,
    val completionRateTrend: TrendDirection
)