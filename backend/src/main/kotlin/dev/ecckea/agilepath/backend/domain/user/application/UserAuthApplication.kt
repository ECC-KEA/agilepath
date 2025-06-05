package dev.ecckea.agilepath.backend.domain.user.application

import dev.ecckea.agilepath.backend.domain.project.service.UserProjectService
import dev.ecckea.agilepath.backend.domain.sprint.model.SprintEventType
import dev.ecckea.agilepath.backend.domain.sprint.service.SprintService
import dev.ecckea.agilepath.backend.domain.user.model.User
import dev.ecckea.agilepath.backend.domain.user.service.UserService
import dev.ecckea.agilepath.backend.shared.logging.events.SprintEventLogger
import dev.ecckea.agilepath.backend.shared.security.UserPrincipal
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class UserAuthApplication (
    private val userService: UserService,
    private val userProjectService: UserProjectService,
    private val sprintService: SprintService,
    private val sprintEventLogger: SprintEventLogger
) {
    fun getCurrentUser(principal: UserPrincipal): User {
        val user = userService.getById(principal.id)
        triggerEventLogging(user.id)
        return user
    }

    private fun triggerEventLogging(userId: String) {
        val projects = userProjectService.getProjectsForUser(userId)
        projects.forEach { project ->
            val sprints = sprintService.getSprints(project.id)
            sprints.forEach { sprint ->
                val today = LocalDate.now()
                val isCompleted = sprint.events.any { it.eventType == SprintEventType.COMPLETED }

                if (sprint.endDate.isBefore(today) && !isCompleted) {
                    sprintEventLogger.logEvent(
                        entityId = sprint.id,
                        eventType = SprintEventType.COMPLETED,
                        triggeredBy = userService.getById(userId),
                        oldValue = null,
                        newValue = sprint.name
                    )
                }
            }
        }

    }
}