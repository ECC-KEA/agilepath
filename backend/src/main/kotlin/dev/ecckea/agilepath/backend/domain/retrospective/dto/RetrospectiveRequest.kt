package dev.ecckea.agilepath.backend.domain.retrospective.dto

import dev.ecckea.agilepath.backend.domain.retrospective.model.TalkingPoint
import java.util.UUID

data class CreateRetrospectiveRequest(
    val sprintId: UUID,
    val talkingPoints: List<TalkingPoint> = emptyList(),
    val teamMood: String? = null,
    val keepDoing: List<String> = emptyList(),
    val stopDoing: List<String> = emptyList(),
    val startDoing: List<String> = emptyList()
)
