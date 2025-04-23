package dev.ecckea.agilepath.backend.domain.sprint.dto

import java.time.LocalDate
import java.util.*

data class SprintRequest(
    val projectId: UUID,
    val name: String,
    val goal: String? = null,
    val startDate: LocalDate,
    val endDate: LocalDate,
)