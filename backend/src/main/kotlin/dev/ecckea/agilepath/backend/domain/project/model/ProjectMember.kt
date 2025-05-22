package dev.ecckea.agilepath.backend.domain.project.model

import dev.ecckea.agilepath.backend.domain.user.model.User

data class ProjectMember(
    val user: User,
    val role: ProjectRole,
)
