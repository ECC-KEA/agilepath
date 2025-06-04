package dev.ecckea.agilepath.backend.domain.retrospective.dto

import dev.ecckea.agilepath.backend.domain.retrospective.model.TalkingPoint
import java.time.Instant
import java.util.UUID

data class RetrospectiveResponse(
    val id: UUID,
    val sprintId: UUID,
    val completedAt: Instant?,
    val talkingPoints: List<TalkingPoint>,
    val teamMood: String?,
    val keepDoing: List<String>,
    val stopDoing: List<String>,
    val startDoing: List<String>
)