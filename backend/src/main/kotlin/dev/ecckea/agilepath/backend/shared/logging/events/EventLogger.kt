package dev.ecckea.agilepath.backend.shared.logging.events

import dev.ecckea.agilepath.backend.domain.user.model.User
import java.util.*

fun interface EventLogger<E : Enum<E>> {
    fun logEvent(
        entityId: UUID,
        eventType: E,
        triggeredBy: User,
        oldValue: String?,
        newValue: String?
    )
}