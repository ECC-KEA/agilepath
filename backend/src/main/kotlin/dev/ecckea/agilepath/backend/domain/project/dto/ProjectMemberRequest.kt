package dev.ecckea.agilepath.backend.domain.project.dto

import dev.ecckea.agilepath.backend.domain.project.model.ProjectRole
import dev.ecckea.agilepath.backend.shared.validation.annotations.TrimmedNotBlank
import dev.ecckea.agilepath.backend.shared.validation.annotations.ValidEnum
import jakarta.validation.constraints.NotNull

data class ProjectMemberRequest(
    @field:TrimmedNotBlank(message = "User ID must not be blank")
    val userId: String,

    @field:NotNull
    @field:ValidEnum(
        enumClass = ProjectRole::class,
        ignoreCase = true,
        message = "must be OWNER, ADMIN or CONTRIBUTOR"
    )
    val role: String,
)
