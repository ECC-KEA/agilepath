package dev.ecckea.agilepath.backend.domain.project.model

import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEntity
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import dev.ecckea.agilepath.backend.shared.utils.now
import java.time.Instant

data class NewProject(
    val name: String,
    val description: String?,
    val framework: Framework,
    val createdBy: String, // UserId
    val createdAt: Instant = now(),
)

fun NewProject.toEntity(createdBy: UserEntity): ProjectEntity = ProjectEntity(
    name = name,
    description = description,
    framework = framework,
    createdBy = createdBy,
    createdAt = createdAt
)