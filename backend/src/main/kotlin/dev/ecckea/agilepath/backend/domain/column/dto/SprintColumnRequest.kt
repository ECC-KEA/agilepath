package dev.ecckea.agilepath.backend.domain.column.dto

import dev.ecckea.agilepath.backend.domain.column.model.SprintColumn
import dev.ecckea.agilepath.backend.domain.column.type.SprintColumnStatus

data class SprintColumnRequest(
    val id: String? = null,
    val sprintId: String,
    val name: String,
    val columnStatus: SprintColumnStatus,
    val columnIndex: Int,
)

fun SprintColumnRequest.toModel(): SprintColumn = SprintColumn(
    id = id ?: "",
    sprintId = sprintId,
    name = name,
    status = columnStatus,
    columnIndex = columnIndex
)
