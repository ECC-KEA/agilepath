package dev.ecckea.agilepath.backend.domain.column.controller

import dev.ecckea.agilepath.backend.domain.column.application.SprintColumnApplication
import dev.ecckea.agilepath.backend.domain.column.dto.SprintColumnRequest
import dev.ecckea.agilepath.backend.domain.column.dto.SprintColumnResponse
import dev.ecckea.agilepath.backend.domain.column.model.mapper.toDTO
import dev.ecckea.agilepath.backend.domain.column.model.mapper.toModel
import dev.ecckea.agilepath.backend.shared.logging.Logged
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Tag(name = "Sprint Column", description = "Endpoints related to sprint column management")
class SprintColumnController(
    private val sprintColumnApplication: SprintColumnApplication
): Logged() {

    @Operation(
        summary = "Get all columns for a sprint",
        description = "Returns a list of all columns associated with the specified sprint ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returned list of sprint columns"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to access this sprint"
            ),
            ApiResponse(responseCode = "404", description = "Not Found – Sprint with specified ID does not exist"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/sprints/{sprintId}/sprint-columns")
    fun getSprintColumns(@PathVariable sprintId: UUID): List<SprintColumnResponse> {
        log.info("GET /sprints/{}/sprint-columns - Fetching columns for sprint", sprintId)
        return sprintColumnApplication.getSprintColumns(sprintId).map { it.toDTO() }
    }

    @Operation(
        summary = "Get column by ID",
        description = "Returns the details of a specific sprint column identified by its ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returned sprint column details"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to access this column"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not Found – Sprint column with specified ID does not exist"
            ),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/sprint-columns/{id}")
    fun getSprintColumn(@PathVariable id: UUID): SprintColumnResponse {
        log.info("GET /sprint-columns/{} - Fetching column by ID", id)
        return sprintColumnApplication.getSprintColumn(id).toDTO()
    }

    @Operation(
        summary = "Create new sprint column",
        description = "Creates a new column for the specified sprint",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully created sprint column"),
            ApiResponse(responseCode = "400", description = "Bad Request – Invalid column data"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to create columns for this sprint"
            ),
            ApiResponse(responseCode = "404", description = "Not Found – Sprint with specified ID does not exist"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PostMapping("/sprint-columns")
    fun createSprintColumn(@RequestBody sprintColumn: SprintColumnRequest): SprintColumnResponse {
        log.info("POST /sprint-columns - Creating new column for sprint {}", sprintColumn.sprintId)
        return sprintColumnApplication.createSprintColumn(sprintColumn.toModel()).toDTO()
    }

    @Operation(
        summary = "Delete sprint column",
        description = "Permanently deletes the specified sprint column",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Successfully deleted sprint column"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to delete this column"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not Found – Sprint column with specified ID does not exist"
            ),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @DeleteMapping("/sprint-columns/{id}")
    fun deleteSprintColumn(@PathVariable id: UUID) {
        log.info("DELETE /sprint-columns/{} - Deleting column", id)
        sprintColumnApplication.deleteSprintColumn(id)
    }

    @Operation(
        summary = "Update sprint column",
        description = "Updates an existing sprint column with the provided details",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully updated sprint column"),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request – Invalid column data or attempt to change sprint ID"
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to update this column"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not Found – Sprint column with specified ID does not exist"
            ),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PutMapping("/sprint-columns/{id}")
    fun updateSprintColumn(
        @PathVariable id: UUID,
        @RequestBody sprintColumn: SprintColumnRequest
    ): SprintColumnResponse {
        log.info("PUT /sprint-columns/{} - Updating column", id)
        return sprintColumnApplication.updateSprintColumn(id, sprintColumn.toModel()).toDTO()
    }
}