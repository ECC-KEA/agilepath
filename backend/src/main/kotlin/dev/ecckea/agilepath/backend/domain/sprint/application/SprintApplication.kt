package dev.ecckea.agilepath.backend.domain.sprint.application

import dev.ecckea.agilepath.backend.domain.sprint.model.NewSprint
import dev.ecckea.agilepath.backend.domain.sprint.model.Sprint
import dev.ecckea.agilepath.backend.domain.sprint.service.SprintService
import dev.ecckea.agilepath.backend.domain.column.service.SprintColumnService
import org.springframework.stereotype.Service
import java.util.*

@Service
class SprintApplication(
    private val sprintService: SprintService,
    private val sprintColumnService: SprintColumnService
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
        
        return createdSprint
    }

    fun updateSprint(sprintId: UUID, sprint: NewSprint): Sprint {
        return sprintService.updateSprint(sprintId, sprint)
    }
}