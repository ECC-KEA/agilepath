package dev.ecckea.agilepath.backend.domain.project.dto

import dev.ecckea.agilepath.backend.domain.project.model.EstimationMethod
import dev.ecckea.agilepath.backend.domain.project.model.Framework
import java.time.ZonedDateTime
import java.util.*

data class ProjectResponse(
    val id: UUID,
    val name: String,
    val description: String?,
    val framework: Framework,
    val estimationMethod: EstimationMethod,
    val createdBy: String?, // UserId
    val createdAt: ZonedDateTime
)
