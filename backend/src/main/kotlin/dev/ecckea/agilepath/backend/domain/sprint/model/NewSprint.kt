package dev.ecckea.agilepath.backend.domain.sprint.model

import java.time.LocalDate
import java.util.*

data class NewSprint(
    val projectId: UUID,
    val name: String,
    val goal: String? = null,
    val teamCapacity: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val copyLastSprintColumns: Boolean = false,
)