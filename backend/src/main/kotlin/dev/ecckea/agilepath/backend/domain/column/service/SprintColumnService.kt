package dev.ecckea.agilepath.backend.domain.column.service

import dev.ecckea.agilepath.backend.domain.column.model.SprintColumn
import dev.ecckea.agilepath.backend.domain.column.repository.SprintColumnRepository
import dev.ecckea.agilepath.backend.domain.column.repository.entity.toEntity
import dev.ecckea.agilepath.backend.domain.column.repository.entity.toModel
import dev.ecckea.agilepath.backend.domain.sprint.repository.SprintRepository
import dev.ecckea.agilepath.backend.shared.coroutines.withIO
import org.springframework.stereotype.Service

@Service
class SprintColumnService(
    private val sprintColumnRepository: SprintColumnRepository,
    private val sprintRepository: SprintRepository,
) {
    suspend fun getSprintColumns(sprintId: String): List<SprintColumn> = withIO {
        sprintColumnRepository.findBySprintId(sprintId).map { it.toModel() }
    }

    suspend fun getSprintColumn(id: String): SprintColumn = withIO {
        sprintColumnRepository.findOneById(id)?.toModel()
            ?: throw IllegalArgumentException("Sprint column with ID $id not found")
    }

    suspend fun createSprintColumn(sprintColumn: SprintColumn): SprintColumn = withIO {
        val sprint = sprintRepository.findOneById(sprintColumn.sprintId)
            ?: throw IllegalArgumentException("Sprint with ID ${sprintColumn.sprintId} not found")
        val entity = sprintColumn.toEntity(sprintColumn, sprint)
        sprintColumnRepository.save(entity)
        entity.toModel()
    }

    suspend fun deleteSprintColumn(id: String) = withIO {
        val sprintColumn = sprintColumnRepository.findOneById(id)
            ?: throw IllegalArgumentException("Sprint column with ID $id not found")
        sprintColumnRepository.delete(sprintColumn)
    }

    suspend fun updateSprintColumn(id: String, sprintColumn: SprintColumn): SprintColumn = withIO {
        val existingSprintColumn = sprintColumnRepository.findOneById(id)
            ?: throw IllegalArgumentException("Sprint column with ID $id not found")
        val sprint = sprintRepository.findOneById(sprintColumn.sprintId)
            ?: throw IllegalArgumentException("Sprint with ID ${sprintColumn.sprintId} not found")
        val updatedSprintColumn = existingSprintColumn.toModel().copy(
            id = id,
            sprintId = sprintColumn.sprintId,
            name = sprintColumn.name,
            status = sprintColumn.status,
            columnIndex = sprintColumn.columnIndex,
        )
        val updatedEntity = updatedSprintColumn.toEntity(updatedSprintColumn, sprint)
        sprintColumnRepository.save(updatedEntity)
        updatedEntity.toModel()
    }


}