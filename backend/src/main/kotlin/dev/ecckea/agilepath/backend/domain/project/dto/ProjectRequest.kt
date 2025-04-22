package dev.ecckea.agilepath.backend.domain.project.dto

import dev.ecckea.agilepath.backend.domain.project.model.Framework

data class ProjectRequest(
    val name: String,
    val description: String?,
    val framework: Framework
)