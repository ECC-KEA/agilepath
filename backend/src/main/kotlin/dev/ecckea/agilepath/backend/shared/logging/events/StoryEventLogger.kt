package dev.ecckea.agilepath.backend.shared.logging.events

import dev.ecckea.agilepath.backend.domain.story.model.StoryEventType
import dev.ecckea.agilepath.backend.domain.story.repository.entity.StoryEventEntity
import dev.ecckea.agilepath.backend.domain.user.model.User
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import org.springframework.stereotype.Component
import java.util.*

@Component
class StoryEventLogger(
    private val ctx: RepositoryContext
) : EventLogger<StoryEventType> {

    override fun logEvent(
        entityId: UUID,
        eventType: StoryEventType,
        triggeredBy: User,
        oldValue: String?,
        newValue: String?
    ) {
        ctx.storyEvent.save(
            StoryEventEntity(
                storyId = entityId,
                eventType = eventType,
                triggeredBy = ctx.user.getReferenceById(triggeredBy.id),
                oldValue = oldValue,
                newValue = newValue
            )
        )
    }
}