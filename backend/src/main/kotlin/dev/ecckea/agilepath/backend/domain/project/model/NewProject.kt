package dev.ecckea.agilepath.backend.domain.project.model

import dev.ecckea.agilepath.backend.shared.utils.now
import java.time.Instant

data class NewProject(
    val name: String,
    val description: String?,
    val framework: Framework,
    val createdBy: String, // UserId
    val createdAt: Instant = now(),
)