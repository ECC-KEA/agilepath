package dev.ecckea.agilepath.backend.domain.sprint.model

import java.time.ZonedDateTime
import java.util.*

data class SprintEvent(
    val id: UUID,
    val sprintId: UUID,
    val eventType: SprintEventType,
    val triggeredBy: String, // UserId
    val oldValue: String? = null,
    val newValue: String? = null,
    val createdAt: ZonedDateTime,
)
