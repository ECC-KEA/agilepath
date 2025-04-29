package dev.ecckea.agilepath.backend.domain.story.dto

import java.util.*

data class CommentRequest(
    val content: String,
    val storyId: UUID?,
    val taskId: UUID?,
)
