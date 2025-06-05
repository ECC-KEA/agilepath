package dev.ecckea.agilepath.backend.domain.analytics.model

import java.time.LocalDate
import java.util.*

data class BurndownData(
    val sprintId: UUID,
    val totalWork: Int,
    val optimalPath: List<BurndownPoint>,
    val actualProgress: List<BurndownPoint>,
    val startDate: LocalDate,
    val endDate: LocalDate
)
