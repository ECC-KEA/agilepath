package dev.ecckea.agilepath.backend.domain.column.dto

data class SprintColumnResponse(
    val id: String,
    val sprintId: String,
    val name: String,
    val status: String,
    val columnIndex: Int,
)
