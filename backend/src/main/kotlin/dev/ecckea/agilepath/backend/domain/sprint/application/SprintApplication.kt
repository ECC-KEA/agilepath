package dev.ecckea.agilepath.backend.domain.sprint.application

import dev.ecckea.agilepath.backend.domain.sprint.model.NewSprint
import dev.ecckea.agilepath.backend.domain.sprint.model.Sprint
import dev.ecckea.agilepath.backend.domain.sprint.service.SprintService
import org.springframework.stereotype.Service
import java.util.*

@Service
class SprintApplication(
    private val sprintService: SprintService,
) {

    fun getSprints(projectId: UUID): List<Sprint> {
        return sprintService.getSprints(projectId)
    }

    fun getSprint(sprintId: UUID): Sprint {
        return sprintService.getSprint(sprintId)
    }

    fun createSprint(sprint: NewSprint): Sprint {
        return sprintService.createSprint(sprint)
    }

    fun updateSprint(sprintId: UUID, sprint: NewSprint): Sprint {
        return sprintService.updateSprint(sprintId, sprint)
    }
}