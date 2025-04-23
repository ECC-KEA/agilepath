package dev.ecckea.agilepath.backend.domain.column.application

import dev.ecckea.agilepath.backend.domain.column.model.NewSprintColumn
import dev.ecckea.agilepath.backend.domain.column.model.SprintColumn
import dev.ecckea.agilepath.backend.domain.column.service.SprintColumnService
import org.springframework.stereotype.Service
import java.util.*

@Service
class SprintColumnApplication(
    private val sprintColumnService: SprintColumnService,
) {
    fun getSprintColumns(sprintId: UUID): List<SprintColumn> {
        return sprintColumnService.getSprintColumns(sprintId)
    }

    fun getSprintColumn(id: UUID): SprintColumn {
        return sprintColumnService.getSprintColumn(id)
    }

    fun createSprintColumn(sprintColumn: NewSprintColumn): SprintColumn {
        return sprintColumnService.createSprintColumn(sprintColumn)
    }

    fun deleteSprintColumn(id: UUID) {
        sprintColumnService.deleteSprintColumn(id)
    }

    fun updateSprintColumn(id: UUID, sprintColumn: NewSprintColumn): SprintColumn {
        return sprintColumnService.updateSprintColumn(id, sprintColumn)
    }
}