package dev.ecckea.agilepath.backend.domain.analytics.dto

import dev.ecckea.agilepath.backend.domain.analytics.model.SprintInfo

data class SprintAnalysisResponse(
    val sprintInfo: SprintInfo,
    val metrics: SprintMetricsResponse,
    val metadata: SprintMetadataResponse,
    val taskMetrics: List<TaskMetricsResponse>,
    val insights: List<SprintInsightResponse>
)