package dev.ecckea.agilepath.backend.domain.column.dto

import dev.ecckea.agilepath.backend.domain.column.model.SprintColumnStatus
import dev.ecckea.agilepath.backend.shared.validation.annotations.TrimmedNotBlank
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.*

data class SprintColumnRequest(
    @field:NotNull(message = "Sprint ID must not be null")
    val sprintId: UUID,

    @field:TrimmedNotBlank(message = "Column name must not be blank or whitespace")
    @field:Size(max = 100, message = "Column name must be at most 100 characters")
    val name: String,

    @field:NotNull(message = "Column status must be provided")
    val columnStatus: SprintColumnStatus,

    @field:Min(value = 0, message = "Column index must be 0 or greater")
    val columnIndex: Int? = null
)