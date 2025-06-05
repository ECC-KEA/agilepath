package dev.ecckea.agilepath.backend.domain.sprint.application

import dev.ecckea.agilepath.backend.domain.column.service.SprintColumnService
import dev.ecckea.agilepath.backend.domain.sprint.model.NewSprint
import dev.ecckea.agilepath.backend.domain.sprint.model.Sprint
import dev.ecckea.agilepath.backend.domain.sprint.model.SprintEventType
import dev.ecckea.agilepath.backend.domain.sprint.service.SprintService
import dev.ecckea.agilepath.backend.domain.user.service.UserService
import dev.ecckea.agilepath.backend.shared.logging.events.SprintEventLogger
import dev.ecckea.agilepath.backend.shared.security.currentUser
import dev.ecckea.agilepath.backend.shared.utils.ChangeDetector
import org.springframework.stereotype.Service
import java.util.*

@Service
class SprintApplication(
    private val sprintService: SprintService,
    private val sprintColumnService: SprintColumnService,
    private val userService: UserService,
    private val eventLogger: SprintEventLogger
) {

    fun getSprints(projectId: UUID): List<Sprint> {
        return sprintService.getSprints(projectId)
    }

    fun getSprint(sprintId: UUID): Sprint {
        return sprintService.getSprint(sprintId)
    }

    fun createSprint(sprint: NewSprint): Sprint {
        val createdSprint = sprintService.createSprint(sprint)
        if(sprint.copyLastSprintColumns) {
            val lastSprint = sprintService.getSprints(sprint.projectId).lastOrNull()
            if (lastSprint != null) {
                sprintColumnService.copyColumnsFromLastSprint(lastSprint.id, createdSprint.id)
            } else {
                sprintColumnService.createDefaultColumns(createdSprint.id)
            }
        } else {
            sprintColumnService.createDefaultColumns(createdSprint.id)
        }

        val user = userService.get(currentUser())

        eventLogger.logEvent(
            entityId = createdSprint.id,
            eventType = SprintEventType.CREATED,
            triggeredBy = user,
            oldValue = null,
            newValue = createdSprint.name
        )

        return createdSprint
    }

    fun updateSprint(sprintId: UUID, sprint: NewSprint): Sprint {
        val updatedSprint = sprintService.updateSprint(sprintId, sprint)
        val oldSprint = sprintService.getSprint(sprintId)
        val user = userService.get(currentUser())
        val changes = ChangeDetector.detectChanges(oldSprint, updatedSprint)

        if (changes.hasChanges()) {
            when {
                changes.contains("goal") -> {
                    eventLogger.logEvent(
                        entityId = sprintId,
                        eventType = SprintEventType.GOAL_CHANGED,
                        triggeredBy = user,
                        oldValue = oldSprint.goal,
                        newValue = sprint.goal
                    )
                }

                changes.contains("endDate") -> {
                    if (sprint.endDate.isAfter(oldSprint.endDate)) {
                        eventLogger.logEvent(
                            entityId = sprintId,
                            eventType = SprintEventType.SPRINT_EXTENDED,
                            triggeredBy = user,
                            oldValue = oldSprint.endDate.toString(),
                            newValue = sprint.endDate.toString()
                        )
                    } else {
                        eventLogger.logEvent(
                            entityId = sprintId,
                            eventType = SprintEventType.SPRINT_SHORTENED,
                            triggeredBy = user,
                            oldValue = oldSprint.endDate.toString(),
                            newValue = sprint.endDate.toString()
                        )
                    }
                }
            }
        }
        return updatedSprint
    }
}