package dev.ecckea.agilepath.backend.domain.column.controller

import dev.ecckea.agilepath.backend.domain.column.application.SprintColumnApplication
import dev.ecckea.agilepath.backend.domain.column.dto.SprintColumnRequest
import dev.ecckea.agilepath.backend.domain.column.dto.SprintColumnResponse
import dev.ecckea.agilepath.backend.domain.column.dto.toModel
import dev.ecckea.agilepath.backend.domain.column.model.toDTO
import dev.ecckea.agilepath.backend.shared.logging.Logged
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sprint-columns")
@Tag(name = "Sprint Column", description = "Endpoints related to sprint column management")
class SprintColumnController(
    private val sprintColumnApplication: SprintColumnApplication
): Logged() {
    @GetMapping("/{sprintId}")
    suspend fun getSprintColumns(@PathVariable sprintId: String): List<SprintColumnResponse> {
        log.info("GET /sprint-columns/{sprintId} - Get sprint columns for sprint")
        return sprintColumnApplication.getSprintColumns(sprintId).map { it.toDTO() }
    }

    @GetMapping("/{id}")
    suspend fun getSprintColumn(@PathVariable id: String): SprintColumnResponse {
        log.info("GET /sprint-columns/{id} - Get sprint column by ID")
        return sprintColumnApplication.getSprintColumn(id).toDTO()
    }

    @PostMapping
    suspend fun createSprintColumn(@RequestBody sprintColumn: SprintColumnRequest): SprintColumnResponse {
        log.info("POST /sprint-columns - Create sprint column")
        return sprintColumnApplication.createSprintColumn(sprintColumn.toModel()).toDTO()
    }

    @DeleteMapping("/{id}")
    suspend fun deleteSprintColumn(@PathVariable id: String) {
        log.info("DELETE /sprint-columns/{id} - Delete sprint column")
        sprintColumnApplication.deleteSprintColumn(id)
    }

    @PutMapping("/{id}")
    suspend fun updateSprintColumn(@PathVariable id: String, @RequestBody sprintColumn: SprintColumnRequest): SprintColumnResponse {
        log.info("PUT /sprint-columns/{id} - Update sprint column")
        return sprintColumnApplication.updateSprintColumn(id, sprintColumn.toModel()).toDTO()
    }

}