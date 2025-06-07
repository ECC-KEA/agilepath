package dev.ecckea.agilepath.backend.domain.analytics.model

data class SprintAnalysis(
    val sprintInfo: SprintInfo,
    val sprintMetrics: SprintMetrics,
    val sprintMetadata: SprintMetadata,
    val taskMetrics: List<TaskMetrics>,
    val insights: List<SprintInsight>
)
