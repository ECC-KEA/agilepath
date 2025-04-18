package dev.ecckea.agilepath.backend.domain.sprint.controller

import dev.ecckea.agilepath.backend.domain.sprint.application.SprintApplication
import dev.ecckea.agilepath.backend.domain.sprint.dto.SprintRequest
import dev.ecckea.agilepath.backend.domain.sprint.dto.SprintResponse
import dev.ecckea.agilepath.backend.domain.sprint.dto.toModel
import dev.ecckea.agilepath.backend.domain.sprint.model.toDTO
import dev.ecckea.agilepath.backend.shared.logging.Logged
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sprint")
@Tag(name = "Sprint", description = "Endpoints related to sprint management")
class SprintController(
    private val sprintApplication: SprintApplication
) : Logged(){
    @GetMapping("/{projectId}")
    suspend fun getSprints(@PathVariable projectId: String): List<SprintResponse> {
        log.info("GET /sprint/{projectId} - Get sprints for project")
        return sprintApplication.getSprints(projectId).map { it.toDTO() }
    }

    @GetMapping("/{id}")
    suspend fun getSprint(@PathVariable id: String): SprintResponse {
        log.info("GET /sprint/{sprintId} - Get sprint by ID")
        return sprintApplication.getSprint(id).toDTO()
    }

    @PostMapping
    suspend fun createSprint(@RequestBody sprint: SprintRequest): SprintResponse {
        log.info("POST /sprint - Create sprint")
        return sprintApplication.createSprint(sprint.toModel()).toDTO()
    }

    @PutMapping("/{id}")
    suspend fun updateSprint(@PathVariable id: String, @RequestBody sprint: SprintRequest): SprintResponse {
        log.info("PUT /sprint/{id} - Update sprint")
        return sprintApplication.updateSprint(id, sprint.toModel()).toDTO()
    }
}