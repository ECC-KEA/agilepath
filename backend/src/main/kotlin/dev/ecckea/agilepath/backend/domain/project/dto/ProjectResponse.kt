package dev.ecckea.agilepath.backend.domain.project.dto

import dev.ecckea.agilepath.backend.domain.project.model.Project
import dev.ecckea.agilepath.backend.domain.project.type.Framework
import java.time.ZonedDateTime

data class ProjectResponse(
    val id: String,
    val name: String,
    val description: String?,
    val framework: Framework,
    val createdBy: String?, // UserId
    val createdAt: ZonedDateTime
)

fun ProjectResponse.toModel() = Project(
    id = id,
    name = name,
    description = description,
    framework = framework,
    createdBy = createdBy,
    createdAt = createdAt.toInstant(),
)