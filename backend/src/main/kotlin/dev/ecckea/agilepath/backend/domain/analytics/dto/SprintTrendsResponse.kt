package dev.ecckea.agilepath.backend.domain.analytics.dto

data class SprintTrendsResponse(
    val sprints: List<SprintMetricsResponse>,
    val avgVelocity: Double,
    val avgCompletionRate: Double,
    val avgCycleTime: Double,
    val avgCollaborationScore: Double,
    val velocityTrend: String,
    val completionRateTrend: String
)
