package dev.ecckea.agilepath.backend.domain.column.service

import dev.ecckea.agilepath.backend.domain.column.model.NewSprintColumn
import dev.ecckea.agilepath.backend.domain.column.model.SprintColumn
import dev.ecckea.agilepath.backend.domain.column.model.mapper.toEntity
import dev.ecckea.agilepath.backend.domain.column.model.mapper.toModel
import dev.ecckea.agilepath.backend.domain.column.model.mapper.updatedWith
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.exceptions.BadRequestException
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class SprintColumnService(
    private val ctx: RepositoryContext
) {
    fun getSprintColumns(sprintId: UUID): List<SprintColumn> {
        return ctx.sprintColumn.findBySprintId(sprintId).map { it.toModel() }
    }

    fun getSprintColumn(id: UUID): SprintColumn {
        return ctx.sprintColumn.findOneById(id)?.toModel()
            ?: throw ResourceNotFoundException("Sprint column with ID $id not found")
    }

    fun createSprintColumn(newSprintColumn: NewSprintColumn): SprintColumn {
        if (!ctx.sprint.existsById(newSprintColumn.sprintId)) {
            throw ResourceNotFoundException("Sprint with ID ${newSprintColumn.sprintId} not found")
        }
        val entity = newSprintColumn.toEntity(ctx)
        val saved = ctx.sprintColumn.save(entity)
        return saved.toModel()
    }

    fun deleteSprintColumn(id: UUID) {
        val sprintColumn = ctx.sprintColumn.findOneById(id)
            ?: throw ResourceNotFoundException("Sprint column with ID $id not found")
        ctx.sprintColumn.delete(sprintColumn)
    }

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
        return savedEntity.toModel()
    }
}