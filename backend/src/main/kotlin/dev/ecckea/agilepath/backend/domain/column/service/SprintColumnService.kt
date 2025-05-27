package dev.ecckea.agilepath.backend.domain.column.service

import dev.ecckea.agilepath.backend.domain.column.model.NewSprintColumn
import dev.ecckea.agilepath.backend.domain.column.model.SprintColumn
import dev.ecckea.agilepath.backend.domain.column.model.mapper.toEntity
import dev.ecckea.agilepath.backend.domain.column.model.mapper.toModel
import dev.ecckea.agilepath.backend.domain.column.model.mapper.updatedWith
import dev.ecckea.agilepath.backend.domain.column.model.mapper.toNewSprintColumn
import dev.ecckea.agilepath.backend.domain.column.model.SprintColumnStatus
import dev.ecckea.agilepath.backend.infrastructure.cache.*
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.exceptions.BadRequestException
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SprintColumnService(
    private val ctx: RepositoryContext,
    private val cacheService: CacheService
) : Logged() {


    @Transactional(readOnly = true)
    fun getSprintColumns(sprintId: UUID): List<SprintColumn> {
        log.info("Fetching columns for sprint $sprintId")

        // Check if the columns are in the cache
        cacheService.getSprintColumns(sprintId)?.let { return it }

        // If not in cache, get from database and cache it
        val columns = ctx.sprintColumn.findBySprintId(sprintId).map { it.toModel() }

        cacheService.cacheSprintColumns(sprintId, columns)
        return columns
    }


    @Transactional(readOnly = true)
    fun getSprintColumn(id: UUID): SprintColumn {
        log.info("Fetching sprint column $id")
        // Check if the sprint column is in the cache
        cacheService.getSprintColumn(id)?.let { return it }

        // If not in cache, get from database and cache it
        return getFromDbAndCache(id)
    }

    @Transactional
    fun createSprintColumn(newSprintColumn: NewSprintColumn): SprintColumn {
        if (!ctx.sprint.existsById(newSprintColumn.sprintId)) {
            throw ResourceNotFoundException("Sprint with ID ${newSprintColumn.sprintId} not found")
        }
        val entity = newSprintColumn.toEntity(ctx)
        val saved = ctx.sprintColumn.save(entity)

        // Invalidate sprint columns cache
        cacheService.invalidateSprintColumns(newSprintColumn.sprintId)

        return saved.toModel()
    }

    @Transactional
    fun updateSprintColumn(id: UUID, sprintColumn: NewSprintColumn): SprintColumn {
        val existingEntity = ctx.sprintColumn.findOneById(id)
            ?: throw ResourceNotFoundException("Sprint column with ID $id not found")

        val sprintId = existingEntity.sprint.id
            ?: throw ResourceNotFoundException("Sprint ID cannot be null.")

        require(sprintId == sprintColumn.sprintId) {
            throw BadRequestException("Sprint ID cannot be changed during update")
        }

        val updatedEntity = existingEntity.updatedWith(sprintColumn)
        val savedEntity = ctx.sprintColumn.save(updatedEntity)

        // Invalidate caches
        cacheService.invalidateSprintColumn(id)
        cacheService.invalidateSprintColumns(sprintId)

        return savedEntity.toModel()
    }


    fun deleteSprintColumn(id: UUID) {
        val sprintColumn = ctx.sprintColumn.findOneById(id)
            ?: throw ResourceNotFoundException("Sprint column with ID $id not found")

        val sprintId = sprintColumn.sprint.id
            ?: throw ResourceNotFoundException("Sprint ID is null on sprint column")

        // Invalidate caches
        cacheService.invalidateSprintColumn(id)
        cacheService.invalidateSprintColumns(sprintId)

        ctx.sprintColumn.delete(sprintColumn)
    }

    @Transactional
    fun copyColumnsFromLastSprint(lastSprintId: UUID, newSprintId: UUID): List<SprintColumn> {
        log.info("Copying columns from last sprint $lastSprintId to new sprint $newSprintId")

        val lastSprintColumns = ctx.sprintColumn.findBySprintId(lastSprintId)
            .map { it.toModel() }

        if (lastSprintColumns.isEmpty()) {
            log.warn("No columns found in last sprint $lastSprintId")
            return emptyList()
        }

        val copiedColumns = lastSprintColumns.map { column ->
            val newColumn = column.copy(sprintId = newSprintId)
            createSprintColumn(newColumn.toNewSprintColumn())
        }

        return copiedColumns
    }

    @Transactional
    fun createDefaultColumns(sprintId: UUID): List<SprintColumn> {
        log.info("Creating default columns for sprint $sprintId")

        val defaultColumns = listOf(
            NewSprintColumn(sprintId, "To Do", SprintColumnStatus.TODO, 0),
            NewSprintColumn(sprintId, "In Progress", SprintColumnStatus.IN_PROGRESS, 1),
            NewSprintColumn(sprintId, "Done", SprintColumnStatus.DONE, 2)
        )

        return defaultColumns.map { createSprintColumn(it) }
    }

    private fun getFromDbAndCache(id: UUID): SprintColumn {
        log.info("Fetching sprint column $id from database")
        val column = ctx.sprintColumn.findOneById(id)?.toModel()
            ?: throw ResourceNotFoundException("Sprint column with ID $id not found")

        cacheService.cacheSprintColumn(column)
        return column
    }
}
