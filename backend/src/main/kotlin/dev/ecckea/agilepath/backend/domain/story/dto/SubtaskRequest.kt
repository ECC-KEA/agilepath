package dev.ecckea.agilepath.backend.domain.story.dto

import java.util.*

data class SubtaskRequest(
    val taskId: UUID,
    val title: String,
    val description: String?,
    val isDone: Boolean
)