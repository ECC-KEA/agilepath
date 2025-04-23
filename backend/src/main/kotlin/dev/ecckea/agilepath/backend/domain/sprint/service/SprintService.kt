package dev.ecckea.agilepath.backend.domain.sprint.service

import dev.ecckea.agilepath.backend.domain.project.repository.ProjectRepository
import dev.ecckea.agilepath.backend.domain.sprint.model.Mapper.toEntity
import dev.ecckea.agilepath.backend.domain.sprint.model.Mapper.toModel
import dev.ecckea.agilepath.backend.domain.sprint.model.Mapper.updatedWith
import dev.ecckea.agilepath.backend.domain.sprint.model.NewSprint
import dev.ecckea.agilepath.backend.domain.sprint.model.Sprint
import dev.ecckea.agilepath.backend.domain.sprint.repository.SprintRepository
import dev.ecckea.agilepath.backend.shared.exceptions.BadRequestException
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.security.currentUser
import dev.ecckea.agilepath.backend.shared.security.toEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class SprintService(
    private val sprintRepository: SprintRepository,
    private val projectRepository: ProjectRepository,
) : Logged() {

    fun getSprints(projectId: UUID): List<Sprint> {
        return sprintRepository.findByProjectId(projectId).map { it.toModel() }
            .sortedBy { it.startDate }
    }

    fun getSprint(sprintId: UUID): Sprint {
        return sprintRepository.findOneById(sprintId)?.toModel()
            ?: throw ResourceNotFoundException("Sprint with ID $sprintId not found")
    }

    fun createSprint(newSprint: NewSprint): Sprint {
        log.info("Creating sprint with name ${newSprint.name}")
        val projectEntity = projectRepository.findOneById(newSprint.projectId)
            ?: throw ResourceNotFoundException("Project with ID ${newSprint.projectId} not found")

        val entity = newSprint.toEntity(project = projectEntity, createdByUser = currentUser().toEntity())
        val saved = sprintRepository.save(entity)
        return saved.toModel()
    }

    fun updateSprint(sprintId: UUID, sprint: NewSprint): Sprint {
        log.info("Updating sprint with ID $sprintId")
        val existingSprint = sprintRepository.findOneById(sprintId)
            ?: throw ResourceNotFoundException("Sprint with ID $sprintId not found")

        require(existingSprint.project.id == sprint.projectId) {
            throw BadRequestException("Project id can not be changed during update")
        }

        val updatedEntity = existingSprint.updatedWith(
            update = sprint,
            modifiedByUser = currentUser().toEntity()
        )

        val updated = sprintRepository.save(updatedEntity)
        return updated.toModel()
    }
}