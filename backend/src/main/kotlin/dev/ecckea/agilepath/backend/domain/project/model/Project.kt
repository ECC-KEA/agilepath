package dev.ecckea.agilepath.backend.domain.project.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import dev.ecckea.agilepath.backend.domain.project.dto.ProjectResponse
import dev.ecckea.agilepath.backend.shared.utils.toZonedDateTime
import java.time.Instant
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
data class Project(
    val id: UUID,
    val name: String,
    val description: String?,
    val framework: Framework,
    val createdBy: String, // UserId
    val createdAt: Instant,
    val modifiedBy: String? = null, // UserId
    val modifiedAt: Instant? = null,

    )

fun Project.toDTO(): ProjectResponse = ProjectResponse(
    id = id,
    name = name,
    description = description,
    framework = framework,
    createdBy = createdBy,
    createdAt = toZonedDateTime(createdAt)
)
