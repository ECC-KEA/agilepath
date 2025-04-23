package dev.ecckea.agilepath.backend.domain.column.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
data class SprintColumn(
    val id: UUID,
    val sprintId: UUID,
    val name: String,
    val status: SprintColumnStatus,
    val columnIndex: Int
)