package dev.ecckea.agilepath.backend.domain.column.model.mapper

import dev.ecckea.agilepath.backend.domain.column.dto.SprintColumnRequest
import dev.ecckea.agilepath.backend.domain.column.dto.SprintColumnResponse
import dev.ecckea.agilepath.backend.domain.column.model.NewSprintColumn
import dev.ecckea.agilepath.backend.domain.column.model.SprintColumn
import dev.ecckea.agilepath.backend.domain.column.repository.entity.SprintColumnEntity
import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEntity
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException

// Entity -> Domain Model
fun SprintColumnEntity.toModel(): SprintColumn {
    val entityId = id ?: throw ResourceNotFoundException("SprintColumn ID cannot be null.")
    val sprintId = sprint.id ?: throw ResourceNotFoundException("Sprint ID cannot be null.")

    return SprintColumn(
        id = entityId,
        sprintId = sprintId,
        name = name,
        status = status,
        columnIndex = columnIndex
    )
}

// Domain Model -> Response DTO
fun SprintColumn.toDTO(): SprintColumnResponse = SprintColumnResponse(
    id = id,
    sprintId = sprintId,
    name = name,
    status = status.toString(),
    columnIndex = columnIndex
)

// Request DTO -> NewSprintColumn (Domain Model)
fun SprintColumnRequest.toModel(): NewSprintColumn = NewSprintColumn(
    sprintId = sprintId,
    name = name,
    status = columnStatus,
    columnIndex = columnIndex
)

// NewSprintColumn -> Entity
fun NewSprintColumn.toEntity(sprint: SprintEntity): SprintColumnEntity = SprintColumnEntity(
    id = null,  // New entity, so ID is null
    sprint = sprint,
    name = name,
    status = status,
    columnIndex = columnIndex
)

// Domain Model -> Entity
fun SprintColumn.toEntity(sprint: SprintEntity): SprintColumnEntity = SprintColumnEntity(
    id = id,
    sprint = sprint,
    name = name,
    status = status,
    columnIndex = columnIndex
)

// Update existing entity with NewSprintColumn data
fun SprintColumnEntity.updatedWith(update: NewSprintColumn): SprintColumnEntity {
    return SprintColumnEntity(
        id = this.id,
        sprint = this.sprint,
        name = update.name,
        status = update.status,
        columnIndex = update.columnIndex
    )
}