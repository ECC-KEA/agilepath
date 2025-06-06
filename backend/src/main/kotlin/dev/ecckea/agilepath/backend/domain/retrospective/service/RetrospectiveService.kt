package dev.ecckea.agilepath.backend.domain.retrospective.service

import dev.ecckea.agilepath.backend.domain.retrospective.dto.RetrospectiveResponse
import dev.ecckea.agilepath.backend.domain.retrospective.model.NewRetrospective
import dev.ecckea.agilepath.backend.domain.retrospective.model.Retrospective
import dev.ecckea.agilepath.backend.domain.retrospective.model.mapper.toEntity
import dev.ecckea.agilepath.backend.domain.retrospective.model.mapper.toModel
import dev.ecckea.agilepath.backend.infrastructure.cache.CacheService
import dev.ecckea.agilepath.backend.infrastructure.cache.cacheRetrospective
import dev.ecckea.agilepath.backend.infrastructure.cache.getRetrospective
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class RetrospectiveService(
    private val ctx: RepositoryContext,
    private val cacheService: CacheService
) : Logged() {

    @Transactional(readOnly = true)
    fun getRetrospective(sprintId: UUID): Retrospective {
        log.info("Fetching retrospective for sprint $sprintId")

        // Check if the retrospective is in the cache
        cacheService.getRetrospective(sprintId)?.let { return it }

        // If not in cache, get from database and cache it
        return getFromDbAndCache(sprintId)
    }

    @Transactional
    fun createRetrospective(retrospective: NewRetrospective): Retrospective {
        log.info("Creating retrospective for sprint ${retrospective.sprintId}")

        // Check if the sprint exists and save sprint entity in variable
        val sprintEntity = ctx.sprint.findById(retrospective.sprintId)
            .orElseThrow {
                ResourceNotFoundException("Sprint with ID ${retrospective.sprintId} not found")
            }

        // Create the retrospective entity
        val retrospectiveEntity = retrospective.toEntity(sprintEntity)
        ctx.retrospective.save(retrospectiveEntity)

        // Convert to model and cache it
        val createdRetrospective = retrospectiveEntity.toModel()
        cacheService.cacheRetrospective(createdRetrospective, 15)
        return createdRetrospective
    }

    private fun getFromDbAndCache(sprintId: UUID): Retrospective {
        log.info("Fetching retrospective from database for sprint $sprintId")

        val entity = ctx.retrospective.findBySprintId(sprintId)
            ?: throw ResourceNotFoundException("Retrospective for sprint $sprintId not found")

        val retrospective = entity.toModel()
        cacheService.cacheRetrospective(retrospective, 15)
        return retrospective
    }
}