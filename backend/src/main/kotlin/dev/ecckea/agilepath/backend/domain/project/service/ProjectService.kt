package dev.ecckea.agilepath.backend.domain.project.service

import dev.ecckea.agilepath.backend.domain.project.model.NewProject
import dev.ecckea.agilepath.backend.domain.project.model.Project
import dev.ecckea.agilepath.backend.domain.project.model.mapper.toEntity
import dev.ecckea.agilepath.backend.domain.project.model.mapper.toModel
import dev.ecckea.agilepath.backend.domain.project.model.mapper.updatedWith
import dev.ecckea.agilepath.backend.domain.user.model.User
import dev.ecckea.agilepath.backend.domain.user.model.mapper.toEntity
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ProjectService(
    private val ctx: RepositoryContext
) : Logged() {

    @Transactional(readOnly = true)
    fun getProject(id: UUID): Project {
        log.info("Fetching project with id $id")
        return ctx.project.findOneById(id)
            ?.toModel()
            ?: throw ResourceNotFoundException("Project with id $id not found")
    }

    @Transactional(readOnly = true)
    fun getProjects(user: User): List<Project> {
        log.info("Fetching projects for user with id {}", user.id)
        return ctx.project.findAllByCreatedBy(user.toEntity())
            .map { it.toModel() }
    }

    @Transactional
    fun createProject(newProject: NewProject, owner: User): Project {
        log.info("Creating project with name ${newProject.name}")
        val entity = newProject.toEntity(ctx)
        val savedEntity = ctx.project.save(entity)
        return savedEntity.toModel()
    }

    @Transactional
    fun deleteProject(id: UUID) {
        log.info("Deleting project with id $id")
        val entity = ctx.project.findOneById(id)
            ?: throw ResourceNotFoundException("Project with id $id not found")
        ctx.project.delete(entity)
    }

    @Transactional
    fun updateProject(id: UUID, updated: NewProject, owner: User): Project {
        log.info("Updating project with id $id")
        val existingEntity = ctx.project.findOneById(id)
            ?: throw ResourceNotFoundException("Project with id $id not found")
        val updatedEntity = existingEntity.updatedWith(updated, owner.id, ctx)
        val savedEntity = ctx.project.save(updatedEntity)
        return savedEntity.toModel()
    }
}
