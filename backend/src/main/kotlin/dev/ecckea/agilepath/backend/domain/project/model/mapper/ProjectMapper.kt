package dev.ecckea.agilepath.backend.domain.project.model.mapper

import dev.ecckea.agilepath.backend.domain.project.dto.ProjectRequest
import dev.ecckea.agilepath.backend.domain.project.dto.ProjectResponse
import dev.ecckea.agilepath.backend.domain.project.model.Framework
import dev.ecckea.agilepath.backend.domain.project.model.NewProject
import dev.ecckea.agilepath.backend.domain.project.model.Project
import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEntity
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.context.repository.ref
import dev.ecckea.agilepath.backend.shared.utils.now
import dev.ecckea.agilepath.backend.shared.utils.toZonedDateTime

fun ProjectEntity.toModel(): Project {
    return id?.let {
        Project(
            id = it,
            name = name,
            description = description,
            framework = framework,
            createdBy = createdBy.id,
            createdAt = createdAt,
            modifiedBy = modifiedBy?.id,
            modifiedAt = modifiedAt,
        )
    } ?: throw IllegalStateException("Cannot convert ProjectEntity to Project model without an ID")
}

fun ProjectEntity.updatedWith(update: NewProject, userId: String, ctx: RepositoryContext): ProjectEntity =
    ProjectEntity(
        id = this.id,
        name = update.name,
        description = update.description,
        framework = update.framework,
        createdBy = this.createdBy,
        modifiedBy = ctx.user.ref(userId),
        createdAt = this.createdAt,
        modifiedAt = now(),
    )

fun ProjectRequest.toModel(userId: String): NewProject = NewProject(
    name = name,
    description = description,
    framework = Framework.fromString(framework),
    createdBy = userId
)

fun Project.toEntity(user: UserEntity): ProjectEntity = ProjectEntity(
    id = id,
    name = name,
    description = description,
    framework = framework,
    createdBy = user,
    createdAt = createdAt,
    modifiedBy = user,
    modifiedAt = modifiedAt
)

fun Project.toDTO(): ProjectResponse = ProjectResponse(
    id = id,
    name = name,
    description = description,
    framework = framework,
    createdBy = createdBy,
    createdAt = toZonedDateTime(createdAt)
)

fun NewProject.toEntity(ctx: RepositoryContext): ProjectEntity = ProjectEntity(
    name = name,
    description = description,
    framework = framework,
    createdBy = ctx.user.ref(createdBy),
    createdAt = createdAt
)