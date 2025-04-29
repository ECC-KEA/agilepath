package dev.ecckea.agilepath.backend.domain.column.dto

import dev.ecckea.agilepath.backend.domain.column.model.SprintColumnStatus
import java.util.*

data class SprintColumnRequest(
    val sprintId: UUID,
    val name: String,
    val columnStatus: SprintColumnStatus,
    val columnIndex: Int? = null,
)