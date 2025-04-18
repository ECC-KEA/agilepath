package dev.ecckea.agilepath.backend.domain.sprint.service

import dev.ecckea.agilepath.backend.domain.project.repository.ProjectRepository
import dev.ecckea.agilepath.backend.domain.sprint.model.Sprint
import dev.ecckea.agilepath.backend.domain.sprint.repository.SprintRepository
import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.toEntity
import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.toModel
import dev.ecckea.agilepath.backend.shared.coroutines.withIO
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.security.currentUser
import dev.ecckea.agilepath.backend.shared.security.toEntity
import org.springframework.stereotype.Service

@Service
class SprintService(
    private val sprintRepository: SprintRepository,
    private val projectRepository: ProjectRepository,
) : Logged() {

    suspend fun getSprints(projectId: String): List<Sprint> = withIO {
        sprintRepository.findByProjectId(projectId).map { it.toModel() }
    }

    suspend fun getSprint(sprintId: String): Sprint = withIO {
        sprintRepository.findOneById(sprintId)?.toModel()
            ?: throw IllegalArgumentException("Sprint with ID $sprintId not found")
    }

    suspend fun createSprint(sprint: Sprint): Sprint = withIO {
        log.info("Creating sprint with name ${sprint.name}")
        val project = projectRepository.findOneById(sprint.projectId)
            ?: throw IllegalArgumentException("Project with ID ${sprint.projectId} not found")
        val entity = sprint.toEntity(project, currentUser().toEntity())
        sprintRepository.save(entity)
        entity.toModel()
    }

    suspend fun updateSprint(sprintId: String, sprint: Sprint): Sprint = withIO {
        log.info("Updating sprint with ID $sprintId")
        val existingSprint = sprintRepository.findOneById(sprintId)
            ?: throw IllegalArgumentException("Sprint with ID $sprintId not found")
        val project = projectRepository.findOneById(sprint.projectId)
            ?: throw IllegalArgumentException("Project with ID ${sprint.projectId} not found")
        val sprintModel = existingSprint.toModel()
        val updatedSprint = sprintModel.copy(
            id = sprintId,
            projectId = sprint.projectId,
            name = sprint.name,
            goal = sprint.goal,
            startDate = sprint.startDate,
            endDate = sprint.endDate,
            createdBy = sprint.createdBy,
            modifiedAt = sprint.modifiedAt,
        )
        val updatedEntity = updatedSprint.toEntity(
            project = project,
            createdBy = existingSprint.createdBy,
            modifiedBy = currentUser().toEntity(),
        )
        sprintRepository.save(updatedEntity)
        updatedEntity.toModel()
    }
}