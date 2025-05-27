package dev.ecckea.agilepath.backend.domain.sprint.dto

import dev.ecckea.agilepath.backend.shared.validation.annotations.NoHtml
import dev.ecckea.agilepath.backend.shared.validation.annotations.TrimmedNotBlank
import dev.ecckea.agilepath.backend.shared.validation.annotations.ValidDateRange
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.util.*

@ValidDateRange(message = "End date must be after start date")
data class SprintRequest(
    @field:NotNull(message = "Project ID must not be null")
    val projectId: UUID,

    @field:TrimmedNotBlank(message = "Sprint name must not be blank")
    @field:Size(min = 3, max = 100, message = "Sprint name must be between 3 and 100 characters")
    val name: String,

    @field:Size(max = 2000, message = "Goal must be at most 2000 characters")
    @field:NoHtml(message = "Goal must not contain HTML")
    val goal: String? = null,

    @field:NotNull(message = "Start date must not be null")
    @field:FutureOrPresent(message = "Start date cannot be in the past")
    val startDate: LocalDate,

    @field:NotNull(message = "End date must not be null")
    val endDate: LocalDate,

    @field:NotNull(message = "Copy last sprint columns must not be null")
    val copyLastSprintColumns: Boolean = true,
)