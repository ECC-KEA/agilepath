package dev.ecckea.agilepath.backend.domain.analytics.model

import java.time.LocalDate

data class BurndownPoint(
    val date: LocalDate,
    val remainingWork: Double
)
