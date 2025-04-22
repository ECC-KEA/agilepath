package dev.ecckea.agilepath.backend.domain.project.service

import dev.ecckea.agilepath.backend.domain.project.model.NewProject
import dev.ecckea.agilepath.backend.domain.project.model.Project
import dev.ecckea.agilepath.backend.domain.project.model.mapper.toEntity
import dev.ecckea.agilepath.backend.domain.project.model.mapper.toModel
import dev.ecckea.agilepath.backend.domain.project.model.mapper.updatedWith
import dev.ecckea.agilepath.backend.domain.project.repository.ProjectRepository
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.security.currentUser
import dev.ecckea.agilepath.backend.shared.security.toEntity

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
) : Logged() {

    @Transactional(readOnly = true)
    fun getProject(id: UUID): Project {
        log.info("Fetching project with id $id")
        return projectRepository.findOneById(id)?.toModel()
            ?: throw ResourceNotFoundException("Project with id $id not found")
    }

    @Transactional
    fun createProject(project: NewProject): Project {
        log.info("Creating project with name ${project.name}")
        val user = currentUser().toEntity()
        val entity = project.toEntity(createdBy = user)
        val saved = projectRepository.save(entity)
        return saved.toModel()
    }

    @Transactional
    fun deleteProject(id: UUID) {
        log.info("Deleting project with id $id")
        val project = projectRepository.findOneById(id)
            ?: throw ResourceNotFoundException("Project with id $id not found")
        return projectRepository.delete(project)
    }

    @Transactional
    fun updateProject(id: UUID, updated: NewProject): Project {
        log.info("Updating project with id $id")

        val existing = projectRepository.findOneById(id)
            ?: throw ResourceNotFoundException("Project with id $id not found")

        val current = currentUser().toEntity()

        val updatedEntity = existing.updatedWith(updated, current)
        val saved = projectRepository.save(updatedEntity)
        return saved.toModel()
    }
}