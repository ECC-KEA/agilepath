package dev.ecckea.agilepath.backend.domain.sprint.service

import dev.ecckea.agilepath.backend.domain.sprint.model.Mapper.toEntity
import dev.ecckea.agilepath.backend.domain.sprint.model.Mapper.toModel
import dev.ecckea.agilepath.backend.domain.sprint.model.Mapper.updatedWith
import dev.ecckea.agilepath.backend.domain.sprint.model.NewSprint
import dev.ecckea.agilepath.backend.domain.sprint.model.Sprint
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.exceptions.BadRequestException
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.security.currentUser
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import java.util.*

@Service
class SprintService(
    private val ctx: RepositoryContext
) : Logged() {

    @Cacheable("sprintsByProject", key = "#projectId")
    fun getSprints(projectId: UUID): List<Sprint> {
        return ctx.sprint.findByProjectId(projectId).map { it.toModel() }
            .sortedBy { it.startDate }
    }

    @Cacheable("sprints", key = "#sprintId")
    fun getSprint(sprintId: UUID): Sprint {
        return ctx.sprint.findOneById(sprintId)?.toModel()
            ?: throw ResourceNotFoundException("Sprint with ID $sprintId not found")
    }

    @CacheEvict(value = ["sprintsByProject"], key = "#newSprint.projectId")
    fun createSprint(newSprint: NewSprint): Sprint {
        log.info("Creating sprint with name ${newSprint.name}")

        if (!ctx.project.existsById(newSprint.projectId)) {
            throw ResourceNotFoundException("Project with ID ${newSprint.projectId} not found")
        }

        val entity = newSprint.toEntity(ctx, currentUser().id)
        val saved = ctx.sprint.save(entity)
        return saved.toModel()
    }

    @Caching(
        evict = [
            CacheEvict(value = ["sprints"], key = "#sprintId"),
            CacheEvict(value = ["sprintsByProject"], key = "#sprint.projectId")
        ]
    )
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
        return updated.toModel()
    }
}