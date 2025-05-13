package dev.ecckea.agilepath.backend.domain.project.service

import dev.ecckea.agilepath.backend.domain.project.model.NewProject
import dev.ecckea.agilepath.backend.domain.project.model.Project
import dev.ecckea.agilepath.backend.domain.project.model.mapper.toEntity
import dev.ecckea.agilepath.backend.domain.project.model.mapper.toModel
import dev.ecckea.agilepath.backend.domain.project.model.mapper.updatedWith
import dev.ecckea.agilepath.backend.domain.user.model.User
import dev.ecckea.agilepath.backend.domain.user.model.mapper.toEntity
import dev.ecckea.agilepath.backend.infrastructure.cache.*
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ProjectService(
    private val ctx: RepositoryContext,
    private val cacheService: CacheService
) : Logged() {

    @Transactional(readOnly = true)
    fun getProject(id: UUID): Project {
        // Check if the project is in the cache
        cacheService.getProject(id)?.let { return it }

        // If not, fetch from the database and cache it
        return getFromDbAndCache(id)
    }

    @Transactional(readOnly = true)
    fun getProjects(user: User): List<Project> {
        log.info("Fetching projects for user with id {}", user.id)
        // Check if the user is in the cache
        cacheService.getUserProjects(user.id)?.let { return it }

        // If not, fetch from the database
        val projects = ctx.project.findAllByCreatedBy(user.toEntity())
            .map { it.toModel() }

        // Cache the projects for the user
        cacheService.cacheUserProjects(user.id, projects)
        return projects
    }

    @Transactional
    fun createProject(newProject: NewProject, owner: User): Project {
        log.info("Creating project with name ${newProject.name}")
        val entity = newProject.toEntity(ctx)
        val savedEntity = ctx.project.save(entity)

        // Invalidate user's projects cache after creating a new project
        cacheService.invalidateUserProjects(owner.id)
        return savedEntity.toModel()
    }

    @Transactional
    fun deleteProject(id: UUID) {
        log.info("Deleting project with id $id")
        val entity = ctx.project.findOneById(id)
            ?: throw ResourceNotFoundException("Project with id $id not found")

        val ownerId = entity.createdBy.id

        // Invalidate caches
        cacheService.invalidateProject(id)
        cacheService.invalidateUserProjects(ownerId)

        ctx.project.delete(entity)
    }

    @Transactional
    fun updateProject(id: UUID, updated: NewProject, owner: User): Project {
        log.info("Updating project with id $id")
        val existingEntity = ctx.project.findOneById(id)
            ?: throw ResourceNotFoundException("Project with id $id not found")
        val updatedEntity = existingEntity.updatedWith(updated, owner.id, ctx)
        val savedEntity = ctx.project.save(updatedEntity)

        // Invalidate caches
        cacheService.invalidateProject(id)
        cacheService.invalidateUserProjects(owner.id)

        return savedEntity.toModel()
    }

    private fun getFromDbAndCache(id: UUID): Project {
        log.info("Fetching project $id from database")
        val project = ctx.project.findOneById(id)?.toModel()
            ?: throw ResourceNotFoundException("Project with id $id not found")

        cacheService.cacheProject(project)
        return project
    }
}