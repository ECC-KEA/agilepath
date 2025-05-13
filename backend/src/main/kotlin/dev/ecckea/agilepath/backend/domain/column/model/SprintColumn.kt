package dev.ecckea.agilepath.backend.domain.column.model

import java.util.*

data class SprintColumn(
    val id: UUID,
    val sprintId: UUID,
    val name: String,
    val status: SprintColumnStatus,
    val columnIndex: Int
)