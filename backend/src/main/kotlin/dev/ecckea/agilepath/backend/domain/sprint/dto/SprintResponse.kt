package dev.ecckea.agilepath.backend.domain.sprint.dto

import java.time.LocalDate
import java.util.*

data class SprintResponse(
    val id: UUID,
    val projectId: UUID,
    val name: String,
    val goal: String?,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val createdBy: String,
)