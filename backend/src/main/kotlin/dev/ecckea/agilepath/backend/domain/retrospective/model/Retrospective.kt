package dev.ecckea.agilepath.backend.domain.retrospective.model

import java.time.Instant
import java.util.UUID

data class Retrospective (
    val id: UUID,
    val sprintId: UUID,
    val completedAt: Instant?,
    val talkingPoints: List<TalkingPoint>,
    val teamMood: String?,
    val keepDoing: List<String>,
    val stopDoing: List<String>,
    val startDoing: List<String>,
)