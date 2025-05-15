package dev.ecckea.agilepath.backend.domain.story.dto

import dev.ecckea.agilepath.backend.domain.story.model.PointEstimate
import dev.ecckea.agilepath.backend.domain.story.model.TshirtEstimate
import dev.ecckea.agilepath.backend.shared.validation.annotations.NoHtml
import dev.ecckea.agilepath.backend.shared.validation.annotations.RequireOneEstimate
import dev.ecckea.agilepath.backend.shared.validation.annotations.TrimmedNotBlank
import dev.ecckea.agilepath.backend.shared.validation.annotations.ValidEnum
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.*

@RequireOneEstimate(message = "Either T-shirt estimate or point estimate must be provided")
data class TaskRequest(
    @field:NotNull(message = "Story ID must not be null")
    val storyId: UUID,
    @field:NotNull(message = "Sprint Column ID must not be null")
    val sprintColumnId: UUID,
    @field:TrimmedNotBlank(message = "Title name must not be blank")
    @field:Size(min = 3, max = 100, message = "Title name must be between 3 and 100 characters")
    val title: String,
    @field:TrimmedNotBlank(message = "Description must not be blank")
    @field:Size(max = 2000, message = "Description must be at most 2000 characters")
    @field:NoHtml(message = "Description must not contain HTML")
    val description: String?,
    @field:ValidEnum(
        enumClass = TshirtEstimate::class,
        ignoreCase = true,
        message = "must be XSMALL, SMALL, MEDIUM, LARGE or XLARGE"
    )
    val estimateTshirt: String?,
    @field:ValidEnum(
        enumClass = PointEstimate::class,
        ignoreCase = true,
        message = "must be POINT_1, POINT_2, POINT_3, POINT_5, POINT_8, POINT_13 or POINT_21"
    )
    val estimatePoints: String?,

    val assigneeIds: List<String>,
)