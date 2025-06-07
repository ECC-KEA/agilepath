package dev.ecckea.agilepath.backend.domain.sprint.model.mapper

import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEntity
import dev.ecckea.agilepath.backend.domain.sprint.dto.SprintRequest
import dev.ecckea.agilepath.backend.domain.sprint.dto.SprintResponse
import dev.ecckea.agilepath.backend.domain.sprint.model.NewSprint
import dev.ecckea.agilepath.backend.domain.sprint.model.Sprint
import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEntity
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.context.repository.ref
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.utils.now

// Entity -> Domain Model
fun SprintEntity.toModel(): Sprint {
    val entityId = id ?: throw ResourceNotFoundException("ID cannot be null.")
    val projectId = project.id ?: throw ResourceNotFoundException("Project ID cannot be null.")

    return Sprint(
        id = entityId,
        projectId = projectId,
        name = name,
        goal = goal,
        teamCapacity = teamCapacity,
        startDate = startDate,
        endDate = endDate,
        createdBy = createdBy.id,
        createdAt = createdAt,
        modifiedBy = modifiedBy?.id,
        modifiedAt = modifiedAt
    )
}

// Domain Model -> Response DTO
fun Sprint.toDTO() = SprintResponse(
    id = id,
    projectId = projectId,
    name = name,
    goal = goal,
    startDate = startDate,
    endDate = endDate,
    createdBy = createdBy
)

// Domain Model -> Entity
fun Sprint.toEntity(
    project: ProjectEntity,
    createdBy: UserEntity,
    modifiedBy: UserEntity? = null
): SprintEntity {
    return SprintEntity(
        id = id,
        project = project,
        name = name,
        goal = goal,
        teamCapacity = teamCapacity,
        startDate = startDate,
        endDate = endDate,
        createdBy = createdBy,
        modifiedBy = modifiedBy,
        createdAt = createdAt,
        modifiedAt = modifiedAt ?: now()
    )
}

// Request DTO -> NewSprint (Domain Model)
fun SprintRequest.toModel() = NewSprint(
    projectId = projectId,
    name = name,
    goal = goal,
    teamCapacity = teamCapacity,
    startDate = startDate,
    endDate = endDate,
    copyLastSprintColumns = copyLastSprintColumns
)

// Create new Entity from NewSprint
fun NewSprint.toEntity(ctx: RepositoryContext, userId: String): SprintEntity = SprintEntity(
    id = null,  // New entity, so ID is null
    project = ctx.project.ref(projectId),
    name = name,
    goal = goal,
    teamCapacity = teamCapacity,
    startDate = startDate,
    endDate = endDate,
    createdBy = ctx.user.ref(userId),
    createdAt = now()
)

// Update existing entity with NewSprint data
fun SprintEntity.updatedWith(update: NewSprint, userId: String, ctx: RepositoryContext): SprintEntity {
    return SprintEntity(
        id = this.id,
        project = this.project,
        name = update.name,
        goal = update.goal,
        teamCapacity = update.teamCapacity,
        startDate = update.startDate,
        endDate = update.endDate,
        createdBy = this.createdBy,
        modifiedBy = ctx.user.ref(userId),
        createdAt = this.createdAt,
        modifiedAt = now()
    )
}