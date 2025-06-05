package dev.ecckea.agilepath.backend.domain.project.dto

import dev.ecckea.agilepath.backend.domain.project.model.EstimationMethod
import dev.ecckea.agilepath.backend.domain.project.model.Framework
import dev.ecckea.agilepath.backend.shared.validation.annotations.NoHtml
import dev.ecckea.agilepath.backend.shared.validation.annotations.TrimmedNotBlank
import dev.ecckea.agilepath.backend.shared.validation.annotations.ValidEnum
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class ProjectRequest(
    @field:TrimmedNotBlank(message = "Project name must not be blank or whitespace only")
    @field:Size(min = 3, max = 100, message = "Project name must be between 3 and 100 characters")
    val name: String,

    @field:NoHtml(message = "Description must not contain HTML")
    @field:Size(max = 2000, message = "Description must be at most 2000 characters")
    val description: String?,

    @field:NotNull(message = "Framework must not be null")
    @field:ValidEnum(enumClass = Framework::class, ignoreCase = true, message = "must be SCRUM, XP, or NONE")
    val framework: String,

    @field:NotNull(message = "Estimation method must not be null")
    @field:ValidEnum(
        enumClass = EstimationMethod::class,
        ignoreCase = true,
        message = "must be STORY_POINTS or TSHIRT_SIZES"
    )
    val estimationMethod: String,
)