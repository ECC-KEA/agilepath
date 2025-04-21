package dev.ecckea.agilepath.backend.domain.project.service

import dev.ecckea.agilepath.backend.domain.project.model.Project
import dev.ecckea.agilepath.backend.domain.project.repository.ProjectRepository
import dev.ecckea.agilepath.backend.domain.project.repository.entity.toEntity
import dev.ecckea.agilepath.backend.domain.project.repository.entity.toModel
import dev.ecckea.agilepath.backend.shared.coroutines.withIO
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.security.currentUser
import dev.ecckea.agilepath.backend.shared.security.toEntity
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
) : Logged() {

    suspend fun getProject(id: UUID): Project = withIO {
        log.info("Fetching project with id $id")
        projectRepository.findOneById(id.toString())?.toModel()
            ?: throw ResourceNotFoundException("Project with id $id not found")
    }

    suspend fun createProject(project: Project): Project = withIO {
        log.info("Creating project with name ${project.name}")
        val entity = project.toEntity(currentUser().toEntity())
        projectRepository.save(entity)
        entity.toModel()
    }

    suspend fun deleteProject(id: String) = withIO {
        log.info("Deleting project with id $id")
        val project = projectRepository.findOneById(id)
            ?: throw ResourceNotFoundException("Project with id $id not found")
        projectRepository.delete(project)
    }

    suspend fun updateProject(id: String, project: Project): Project = withIO {
        log.info("Updating project with id $id")
        val existingProject = projectRepository.findOneById(id)
            ?: throw ResourceNotFoundException("Project with id $id not found")
        val projectModel = existingProject.toModel()

        val updatedProject = projectModel.copy(
            name = project.name,
            description = project.description,
            framework = project.framework,
            modifiedAt = Instant.now(),
        )
        val updatedEntity = updatedProject.toEntity(
            createdBy = existingProject.createdBy,
            modifiedBy = currentUser().toEntity(),
        )
        projectRepository.save(updatedEntity)
        updatedEntity.toModel()
    }
}