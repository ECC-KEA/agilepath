package dev.ecckea.agilepath.backend.domain.project.model.mapper

import dev.ecckea.agilepath.backend.domain.project.dto.ProjectRequest
import dev.ecckea.agilepath.backend.domain.project.dto.ProjectResponse
import dev.ecckea.agilepath.backend.domain.project.model.NewProject
import dev.ecckea.agilepath.backend.domain.project.model.Project
import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEntity
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
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

fun ProjectEntity.updatedWith(update: NewProject, modifiedBy: UserEntity): ProjectEntity =
    ProjectEntity(
        id = this.id,
        name = update.name,
        description = update.description,
        framework = update.framework,
        createdBy = this.createdBy,
        modifiedBy = modifiedBy,
        createdAt = this.createdAt,
        modifiedAt = now(),
    )

fun ProjectRequest.toModel(userId: String): NewProject = NewProject(
    name = name,
    description = description,
    framework = framework,
    createdBy = userId
)

fun Project.toDTO(): ProjectResponse = ProjectResponse(
    id = id,
    name = name,
    description = description,
    framework = framework,
    createdBy = createdBy,
    createdAt = toZonedDateTime(createdAt)
)

fun NewProject.toEntity(createdBy: UserEntity): ProjectEntity = ProjectEntity(
    name = name,
    description = description,
    framework = framework,
    createdBy = createdBy,
    createdAt = createdAt
)