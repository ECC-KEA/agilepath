package dev.ecckea.agilepath.backend.domain.column.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import dev.ecckea.agilepath.backend.domain.column.dto.SprintColumnResponse
import dev.ecckea.agilepath.backend.domain.column.type.SprintColumnStatus

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
data class SprintColumn(
    val id: String,
    val sprintId: String,
    val name: String,
    val status: SprintColumnStatus,
    val columnIndex: Int
)

fun SprintColumn.toDTO() = SprintColumnResponse(
    id = id,
    sprintId = sprintId,
    name = name,
    status = status.name,
    columnIndex = columnIndex
)