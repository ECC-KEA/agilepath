package dev.ecckea.agilepath.backend.domain.retrospective.model

import java.util.UUID

data class NewRetrospective(
    val sprintId: UUID,
    val talkingPoints: List<TalkingPoint> = emptyList(),
    val teamMood: String? = null,
    val keepDoing: List<String> = emptyList(),
    val stopDoing: List<String> = emptyList(),
    val startDoing: List<String> = emptyList()
)
