package dev.ecckea.agilepath.backend.shared.logging.events

import dev.ecckea.agilepath.backend.domain.story.model.TaskEventType
import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskEventEntity
import dev.ecckea.agilepath.backend.domain.user.model.User
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import org.springframework.stereotype.Component
import java.util.*

@Component
class TaskEventLogger(
    private val ctx: RepositoryContext,
) : EventLogger<TaskEventType> {

    override fun logEvent(
        entityId: UUID,
        eventType: TaskEventType,
        triggeredBy: User,
        oldValue: String?,
        newValue: String?
    ) {
        ctx.taskEvent.save(
            TaskEventEntity(
                taskId = entityId,
                eventType = eventType,
                triggeredBy = ctx.user.getReferenceById(triggeredBy.id),
                oldValue = oldValue,
                newValue = newValue
            )
        )
    }
}