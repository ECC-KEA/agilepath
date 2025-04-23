package dev.ecckea.agilepath.backend.domain.column.service

import dev.ecckea.agilepath.backend.domain.column.model.NewSprintColumn
import dev.ecckea.agilepath.backend.domain.column.model.SprintColumn
import dev.ecckea.agilepath.backend.domain.column.model.mapper.toEntity
import dev.ecckea.agilepath.backend.domain.column.model.mapper.toModel
import dev.ecckea.agilepath.backend.domain.column.model.mapper.updatedWith
import dev.ecckea.agilepath.backend.domain.column.repository.SprintColumnRepository
import dev.ecckea.agilepath.backend.domain.sprint.repository.SprintRepository
import dev.ecckea.agilepath.backend.shared.exceptions.BadRequestException
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class SprintColumnService(
    private val sprintColumnRepository: SprintColumnRepository,
    private val sprintRepository: SprintRepository,
) {
    fun getSprintColumns(sprintId: UUID): List<SprintColumn> {
        return sprintColumnRepository.findBySprintId(sprintId).map { it.toModel() }
    }

    fun getSprintColumn(id: UUID): SprintColumn {
        return sprintColumnRepository.findOneById(id)?.toModel()
            ?: throw ResourceNotFoundException("Sprint column with ID $id not found")
    }

    fun createSprintColumn(newSprintColumn: NewSprintColumn): SprintColumn {
        val sprint = sprintRepository.findOneById(newSprintColumn.sprintId)
            ?: throw ResourceNotFoundException("Sprint with ID ${newSprintColumn.sprintId} not found")
        val entity = newSprintColumn.toEntity(sprint)
        val saved = sprintColumnRepository.save(entity)
        return saved.toModel()
    }

    fun deleteSprintColumn(id: UUID) {
        val sprintColumn = sprintColumnRepository.findOneById(id)
            ?: throw ResourceNotFoundException("Sprint column with ID $id not found")
        sprintColumnRepository.delete(sprintColumn)
    }

    fun updateSprintColumn(id: UUID, sprintColumn: NewSprintColumn): SprintColumn {
        val existingEntity = sprintColumnRepository.findOneById(id)
            ?: throw ResourceNotFoundException("Sprint column with ID $id not found")

        val sprintId = existingEntity.sprint.id
            ?: throw ResourceNotFoundException("Sprint ID cannot be null.")

        require(sprintId == sprintColumn.sprintId) {
            throw BadRequestException("Sprint ID cannot be changed during update")
        }

        val updatedEntity = existingEntity.updatedWith(sprintColumn)

        val savedEntity = sprintColumnRepository.save(updatedEntity)
        return savedEntity.toModel()
    }
}