package dev.ecckea.agilepath.backend.domain.column.dto

import java.util.*

data class SprintColumnResponse(
    val id: UUID,
    val sprintId: UUID,
    val name: String,
    val status: String,
    val columnIndex: Int,
)
