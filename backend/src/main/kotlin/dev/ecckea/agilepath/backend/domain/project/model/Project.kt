package dev.ecckea.agilepath.backend.domain.project.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import dev.ecckea.agilepath.backend.domain.project.dto.ProjectResponse
import dev.ecckea.agilepath.backend.domain.project.type.Framework
import dev.ecckea.agilepath.backend.shared.utils.toZonedDateTime
import java.time.Instant

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
data class Project(
    val id: String,
    val name: String,
    val description: String?,
    val framework: Framework,
    val createdBy: String?, // UserId
    val createdAt: Instant = Instant.now(),
    val modifiedAt: Instant? = null,
)

fun Project.toDTO() = ProjectResponse(
    id = id,
    name = name,
    description = description,
    framework = framework,
    createdBy = createdBy,
    createdAt = toZonedDateTime(createdAt)
)

