package dev.ecckea.agilepath.backend.domain.sprint.controller

import dev.ecckea.agilepath.backend.domain.sprint.application.SprintApplication
import dev.ecckea.agilepath.backend.domain.sprint.dto.SprintRequest
import dev.ecckea.agilepath.backend.domain.sprint.dto.SprintResponse
import dev.ecckea.agilepath.backend.domain.sprint.model.mapper.toDTO
import dev.ecckea.agilepath.backend.domain.sprint.model.mapper.toModel
import dev.ecckea.agilepath.backend.shared.logging.Logged
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Validated
@Tag(name = "Sprint", description = "Endpoints related to sprint management")
class SprintController(
    private val sprintApplication: SprintApplication
) : Logged() {
    @Operation(
        summary = "Get all sprints for a project",
        description = "Returns a list of all sprints associated with the specified project ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returned list of sprints"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to access this project"
            ),
            ApiResponse(responseCode = "404", description = "Not Found – Project with specified ID does not exist"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/projects/{projectId}/sprints")
    fun getSprints(@PathVariable projectId: UUID): List<SprintResponse> {
        log.info("GET /projects/{}/sprints - Get sprints for project", projectId)
        return sprintApplication.getSprints(projectId).map { it.toDTO() }
    }

    @Operation(
        summary = "Get sprint by ID",
        description = "Returns the details of a specific sprint identified by its ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returned sprint details"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to access this sprint"
            ),
            ApiResponse(responseCode = "404", description = "Not Found – Sprint with specified ID does not exist"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/sprints/{id}")
    fun getSprint(@PathVariable id: UUID): SprintResponse {
        log.info("GET /sprints/{} - Get sprint by ID", id)
        return sprintApplication.getSprint(id).toDTO()
    }

    @Operation(
        summary = "Create new sprint",
        description = "Creates a new sprint with the provided details",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully created sprint"),
            ApiResponse(responseCode = "400", description = "Bad Request – Invalid sprint data"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to create sprints for this project"
            ),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PostMapping("/sprints")
    fun createSprint(@Valid @RequestBody sprint: SprintRequest): SprintResponse {
        log.info("POST /sprints - Create sprint")
        return sprintApplication.createSprint(sprint.toModel()).toDTO()
    }

    @Operation(
        summary = "Update sprint",
        description = "Updates an existing sprint with the provided details",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully updated sprint"),
            ApiResponse(responseCode = "400", description = "Bad Request – Invalid sprint data"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to update this sprint"
            ),
            ApiResponse(responseCode = "404", description = "Not Found – Sprint with specified ID does not exist"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PutMapping("/sprints/{id}")
    fun updateSprint(@PathVariable id: UUID, @Valid @RequestBody sprint: SprintRequest): SprintResponse {
        log.info("PUT /sprints/{} - Update sprint", id)
        return sprintApplication.updateSprint(id, sprint.toModel()).toDTO()
    }


    @Operation(
        summary = "End sprint, sets end date to now",
        description = "Ends the specified sprint by setting its end date to the current date",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully ended sprint"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to end this sprint"
            ),
            ApiResponse(responseCode = "404", description = "Not Found – Sprint with specified ID does not exist"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PatchMapping("/sprints/{id}/end")
    fun endSprint(@PathVariable id: UUID): SprintResponse {
        log.info("PATCH /sprints/{} - End sprint", id)
        return sprintApplication.endSprint(id).toDTO()
    }
}