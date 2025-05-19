package dev.ecckea.agilepath.backend.domain.story.dto

import dev.ecckea.agilepath.backend.shared.validation.annotations.NoHtml
import dev.ecckea.agilepath.backend.shared.validation.annotations.TrimmedNotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.*

data class SubtaskRequest(
    @field:NotNull(message = "Task ID must not be null")
    val taskId: UUID,
    @field:TrimmedNotBlank(message = "Title name must not be blank")
    @field:Size(min = 3, max = 100, message = "Title name must be between 3 and 100 characters")
    val title: String,
    @field:TrimmedNotBlank(message = "Description must not be blank")
    @field:Size(max = 2000, message = "Description must be at most 2000 characters")
    @field:NoHtml(message = "Description must not contain HTML")
    val description: String?,
    val isDone: Boolean
)