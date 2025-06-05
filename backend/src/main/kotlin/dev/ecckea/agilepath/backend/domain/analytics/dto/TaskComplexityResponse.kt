package dev.ecckea.agilepath.backend.domain.analytics.dto

data class TaskComplexityResponse(
    val simple: Int,
    val moderate: Int,
    val complex: Int
)
