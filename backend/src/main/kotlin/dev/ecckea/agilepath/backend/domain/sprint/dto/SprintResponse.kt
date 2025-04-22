package dev.ecckea.agilepath.backend.domain.sprint.dto

data class SprintResponse(
    val id: String,
    val projectId: String,
    val name: String,
    val goal: String?,
    val startDate: String?,
    val endDate: String?,
    val createdBy: String,
)