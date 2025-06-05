package dev.ecckea.agilepath.backend.domain.project.model

import java.time.Instant
import java.util.*

data class Project(
    val id: UUID,
    val name: String,
    val description: String?,
    val framework: Framework,
    val estimationMethod: EstimationMethod,
    val createdBy: String, // UserId
    val createdAt: Instant,
    val modifiedBy: String? = null, // UserId
    val modifiedAt: Instant? = null,
    )
