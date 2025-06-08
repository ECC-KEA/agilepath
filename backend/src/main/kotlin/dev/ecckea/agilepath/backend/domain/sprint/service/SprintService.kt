package dev.ecckea.agilepath.backend.domain.sprint.service

import dev.ecckea.agilepath.backend.domain.sprint.model.NewSprint
import dev.ecckea.agilepath.backend.domain.sprint.model.Sprint
import dev.ecckea.agilepath.backend.domain.sprint.model.mapper.toEntity
import dev.ecckea.agilepath.backend.domain.sprint.model.mapper.toModel
import dev.ecckea.agilepath.backend.domain.sprint.model.mapper.updatedWith
import dev.ecckea.agilepath.backend.infrastructure.cache.*
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.exceptions.BadRequestException
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.security.currentUser
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*

@Service
class SprintService(
    private val ctx: RepositoryContext,
    private val cacheService: CacheService
) : Logged() {
    @Transactional(readOnly = true)
    fun getSprints(projectId: UUID): List<Sprint> {
        log.info("Fetching sprints for project $projectId")

        // Check if the sprints are in the cache
        cacheService.getProjectSprints(projectId)?.let { return it }

        // If not in cache, get from the database and cache it
        val sprints = ctx.sprint.findByProjectId(projectId)
            .map { it.toModel() }
            .sortedBy { it.startDate }

        cacheService.cacheProjectSprints(projectId, sprints)
        return sprints
    }


    @Transactional(readOnly = true)
    fun getSprint(sprintId: UUID): Sprint {
        log.info("Fetching sprint $sprintId")

        // Check if the sprint is in the cache
        cacheService.getSprint(sprintId)?.let { return it }

        // If not in cache, get from the database and cache it
        return getFromDbAndCache(sprintId)
    }

    @Transactional
    fun createSprint(newSprint: NewSprint): Sprint {
        log.info("Creating sprint with name ${newSprint.name}")

        if (!ctx.project.existsById(newSprint.projectId)) {
            throw ResourceNotFoundException("Project with ID ${newSprint.projectId} not found")
        }

        val entity = newSprint.toEntity(ctx, currentUser().id)
        val saved = ctx.sprint.save(entity)
        val sprint = saved.toModel()

        // Invalidate project sprints cache
        cacheService.invalidateProjectSprints(newSprint.projectId)

        return sprint
    }

    @Transactional
    fun updateSprint(sprintId: UUID, sprint: NewSprint): Sprint {
        log.info("Updating sprint with ID $sprintId")

        val existingSprint = ctx.sprint.findOneById(sprintId)
            ?: throw ResourceNotFoundException("Sprint with ID $sprintId not found")

        require(existingSprint.project.id == sprint.projectId) {
            throw BadRequestException("Project id can not be changed during update")
        }

        val updatedEntity = existingSprint.updatedWith(
            update = sprint,
            userId = currentUser().id,
            ctx = ctx
        )

        val updated = ctx.sprint.save(updatedEntity)
        val updatedSprint = updated.toModel()

        // Invalidate caches
        cacheService.invalidateSprint(sprintId)
        cacheService.invalidateProjectSprints(sprint.projectId)

        return updatedSprint
    }

    @Transactional
    fun endSprint(sprintId: UUID): Sprint {
        log.info("Ending sprint with ID $sprintId")

        val existingSprint = ctx.sprint.findOneById(sprintId)
            ?: throw ResourceNotFoundException("Sprint with ID $sprintId not found")

        if (existingSprint.endDate.isBefore(LocalDate.now()) || existingSprint.endDate == LocalDate.now()) {
            throw BadRequestException("Sprint with ID $sprintId is already ended")
        }

        val existingSprintModel = existingSprint.toModel()

        val updatedEntity = existingSprint.updatedWith(
            update = NewSprint(
                projectId = existingSprintModel.projectId,
                name = existingSprintModel.name,
                goal = existingSprintModel.goal,
                startDate = existingSprintModel.startDate,
                endDate = LocalDate.now(),
                teamCapacity = existingSprintModel.teamCapacity,
            ),
            userId = currentUser().id,
            ctx = ctx
        )

        val updated = ctx.sprint.save(updatedEntity)
        val updatedSprint = updated.toModel()

        // Invalidate caches
        cacheService.invalidateSprint(sprintId)
        cacheService.invalidateProjectSprints(existingSprintModel.projectId)

        return updatedSprint
    }

    private fun getFromDbAndCache(id: UUID): Sprint {
        log.info("Fetching sprint $id from database")
        val sprint = ctx.sprint.findOneById(id)?.toModel()
            ?: throw ResourceNotFoundException("Sprint with ID $id not found")

        cacheService.cacheSprint(sprint)
        return sprint
    }
}