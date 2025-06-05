package dev.ecckea.agilepath.backend.shared.logging.events

import dev.ecckea.agilepath.backend.domain.sprint.model.SprintEventType
import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEventEntity
import dev.ecckea.agilepath.backend.domain.user.model.User
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import org.springframework.stereotype.Component
import java.util.*

@Component
class SprintEventLogger(
    private val ctx: RepositoryContext
) : EventLogger<SprintEventType> {

    override fun logEvent(
        entityId: UUID,
        eventType: SprintEventType,
        triggeredBy: User,
        oldValue: String?,
        newValue: String?
    ) {
        ctx.sprintEvent.save(
            SprintEventEntity(
                sprintId = entityId,
                eventType = eventType,
                triggeredBy = ctx.user.getReferenceById(triggeredBy.id),
                oldValue = oldValue,
                newValue = newValue
            )
        )
    }
}