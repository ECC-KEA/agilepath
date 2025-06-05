package dev.ecckea.agilepath.backend.shared.logging.events

import dev.ecckea.agilepath.backend.domain.project.model.ProjectEventType
import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEventEntity
import dev.ecckea.agilepath.backend.domain.user.model.User
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import org.springframework.stereotype.Component
import java.util.*

@Component
class ProjectEventLogger(
    private val ctx: RepositoryContext
) : EventLogger<ProjectEventType> {

    override fun logEvent(
        entityId: UUID,
        eventType: ProjectEventType,
        triggeredBy: User,
        oldValue: String?,
        newValue: String?
    ) {
        ctx.projectEvent.save(
            ProjectEventEntity(
                projectId = entityId,
                eventType = eventType,
                triggeredBy = ctx.user.getReferenceById(triggeredBy.id),
                oldValue = oldValue,
                newValue = newValue
            )
        )
    }
}