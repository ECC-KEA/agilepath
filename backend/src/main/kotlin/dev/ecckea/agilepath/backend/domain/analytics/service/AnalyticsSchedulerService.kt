package dev.ecckea.agilepath.backend.domain.analytics.service

import dev.ecckea.agilepath.backend.domain.sprint.model.SprintEventType
import dev.ecckea.agilepath.backend.domain.user.service.UserService
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.logging.events.SprintEventLogger
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class AnalyticsSchedulerService(
    private val ctx: RepositoryContext,
    private val eventLogger: SprintEventLogger,
    private val userService: UserService
) : Logged() {

    /**
     * Auto-complete sprints that have passed their end date
     * Runs daily at 02:00 AM
     */
    @Scheduled(cron = "0 0 2 * * *")
    fun autoCompleteOverdueSprints() {
        log.info("Running scheduled task to auto-complete overdue sprints")
        val systemUser = userService.getOrCreateSystemUser()
        val sprints = ctx.sprint.findOverdueSprints()

        if (sprints.isEmpty()) {
            log.info("No overdue sprints found")
            return
        }

        sprints.forEach { sprint ->
            val sprintId = sprint.id ?: throw IllegalStateException("Sprint ID is null for sprint: ${sprint.name}")
            log.info("Completing overdue sprint: ${sprint.name} (ID: ${sprint.id})")

            // Log the completion event
            eventLogger.logEvent(
                entityId = sprintId,
                eventType = SprintEventType.COMPLETED,
                triggeredBy = systemUser,
                oldValue = sprint.name,
                newValue = sprint.name,
            )
        }
    }
}