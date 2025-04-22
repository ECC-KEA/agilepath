package dev.ecckea.agilepath.backend.domain.project.dto

import dev.ecckea.agilepath.backend.domain.project.model.Framework
import dev.ecckea.agilepath.backend.domain.project.model.NewProject


data class ProjectRequest(
    val name: String,
    val description: String?,
    val framework: Framework
)

fun ProjectRequest.toModel(userId: String): NewProject = NewProject(
    name = name,
    description = description,
    framework = framework,
    createdBy = userId
)