package dev.ecckea.agilepath.backend.domain.sprint.application
import dev.ecckea.agilepath.backend.domain.sprint.model.Sprint
import dev.ecckea.agilepath.backend.domain.sprint.service.SprintService
import org.springframework.stereotype.Service

@Service
class SprintApplication(
    private val sprintService: SprintService,
) {

    suspend fun getSprints(projectId: String): List<Sprint> {
        return sprintService.getSprints(projectId)
    }

    suspend fun getSprint(sprintId: String): Sprint {
        return sprintService.getSprint(sprintId)
    }

    suspend fun createSprint(sprint: Sprint): Sprint {
        return sprintService.createSprint(sprint)
    }

    suspend fun updateSprint(sprintId: String, sprint: Sprint): Sprint {
        return sprintService.updateSprint(sprintId, sprint)
    }
}