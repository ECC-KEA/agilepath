package dev.ecckea.agilepath.backend.domain.story.dto

import dev.ecckea.agilepath.backend.shared.validation.annotations.NoHtml
import dev.ecckea.agilepath.backend.shared.validation.annotations.TrimmedNotBlank
import jakarta.validation.constraints.Size
import java.util.*

data class CommentRequest(

    @field:TrimmedNotBlank(message = "Content must not be blank")
    @field:Size(max = 2000, message = "Content must be at most 2000 characters")
    @field:NoHtml(message = "Content must not contain HTML")
    val content: String,
    val storyId: UUID?,
    val taskId: UUID?,
)
