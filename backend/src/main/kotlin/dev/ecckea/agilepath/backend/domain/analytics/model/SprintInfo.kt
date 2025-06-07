package dev.ecckea.agilepath.backend.domain.analytics.model

import java.time.LocalDate
import java.util.*

data class SprintInfo(
    val id: UUID,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)