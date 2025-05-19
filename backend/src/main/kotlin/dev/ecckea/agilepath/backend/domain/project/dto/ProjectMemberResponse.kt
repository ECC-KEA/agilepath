package dev.ecckea.agilepath.backend.domain.project.dto

import dev.ecckea.agilepath.backend.domain.project.model.ProjectRole
import dev.ecckea.agilepath.backend.domain.user.dto.UserResponse

data class ProjectMemberResponse(
    val user: UserResponse,
    val role: ProjectRole,
)
