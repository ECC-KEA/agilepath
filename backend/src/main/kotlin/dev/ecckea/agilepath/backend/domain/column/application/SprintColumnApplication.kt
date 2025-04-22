package dev.ecckea.agilepath.backend.domain.column.application

import dev.ecckea.agilepath.backend.domain.column.model.SprintColumn
import dev.ecckea.agilepath.backend.domain.column.service.SprintColumnService
import org.springframework.stereotype.Service

@Service
class SprintColumnApplication(
    private val sprintColumnService: SprintColumnService,
) {
    suspend fun getSprintColumns(sprintId: String): List<SprintColumn> {
        return sprintColumnService.getSprintColumns(sprintId)
    }

    suspend fun getSprintColumn(id: String): SprintColumn {
        return sprintColumnService.getSprintColumn(id)
    }

    suspend fun createSprintColumn(sprintColumn: SprintColumn): SprintColumn {
        return sprintColumnService.createSprintColumn(sprintColumn)
    }

    suspend fun deleteSprintColumn(id: String) {
        sprintColumnService.deleteSprintColumn(id)
    }

    suspend fun updateSprintColumn(id: String, sprintColumn: SprintColumn): SprintColumn {
        return sprintColumnService.updateSprintColumn(id, sprintColumn)
    }
}