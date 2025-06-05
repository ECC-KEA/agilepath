package dev.ecckea.agilepath.backend.domain.story.dto

import dev.ecckea.agilepath.backend.shared.validation.annotations.NoHtml
import dev.ecckea.agilepath.backend.shared.validation.annotations.TrimmedNotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import java.util.*

data class StoryRequest(
    @field:NotNull(message = "Project ID must not be null")
    val projectId: UUID,
    @field:TrimmedNotBlank(message = "Title name must not be blank")
    @field:Size(min = 3, max = 100, message = "Title name must be between 3 and 100 characters")
    val title: String,
    
    @field:Size(max = 2000, message = "Description must be at most 2000 characters")
    @field:NoHtml(message = "Description must not contain HTML")
    val description: String?,

    @field:Size(max = 2000, message = "Acceptance criteria must be at most 2000 characters")
    @field:NoHtml(message = "Acceptance criteria must not contain HTML")
    val acceptanceCriteria: String?,
    
    @field:TrimmedNotBlank(message = "Status must not be blank")
    val status: String,
    @field:PositiveOrZero(message = "Priority must be 0 or greater")
    val priority: Int
)
