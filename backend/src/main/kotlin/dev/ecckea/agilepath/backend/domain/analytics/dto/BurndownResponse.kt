package dev.ecckea.agilepath.backend.domain.analytics.dto

import dev.ecckea.agilepath.backend.domain.analytics.model.BurndownPoint
import java.time.LocalDate
import java.util.*

data class BurndownResponse(
    val sprintId: UUID,
    val totalWork: Int,
    val optimalPath: List<BurndownPoint>,
    val actualProgress: List<BurndownPoint>,
    val startDate: LocalDate,
    val endDate: LocalDate
)
