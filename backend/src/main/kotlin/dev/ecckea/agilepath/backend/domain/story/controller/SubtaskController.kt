package dev.ecckea.agilepath.backend.domain.story.controller

import dev.ecckea.agilepath.backend.domain.story.application.SubtaskApplication
import dev.ecckea.agilepath.backend.domain.story.dto.SubtaskRequest
import dev.ecckea.agilepath.backend.domain.story.dto.SubtaskResponse
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toDTO
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toModel
import dev.ecckea.agilepath.backend.shared.logging.Logged
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/subtasks")
@Tag(name = "Subtasks", description = "Endpoints related to Subtask management")
class SubtaskController(
    private val subtaskApplication: SubtaskApplication
) : Logged() {

    @Operation(
        summary = "Create a new subtask",
        description = "Creates a new subtask with the provided details",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully created subtask"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "403", description = "Forbidden – Not allowed to create a subtask"),
            ApiResponse(responseCode = "404", description = "Not Found – Task not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PostMapping
    fun createSubtask(@RequestBody subtaskRequest: SubtaskRequest): SubtaskResponse {
        log.info("POST /subtasks - Create subtask")
        return subtaskApplication.createSubtask(subtaskRequest.toModel()).toDTO()
    }

    @Operation(
        summary = "Get subtask by ID",
        description = "Returns the subtask details for the specified subtask ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returned subtask details"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "403", description = "Forbidden – Not allowed to access this subtask"),
            ApiResponse(responseCode = "404", description = "Not Found – Subtask not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/{id}")
    fun getSubtask(@PathVariable id: UUID): SubtaskResponse {
        log.info("GET /subtasks/$id - Get subtask by ID")
        return subtaskApplication.getSubtask(id).toDTO()
    }

    @Operation(
        summary = "Get subtasks by task ID",
        description = "Returns all subtasks for the specified task ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returned subtasks"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/task/{taskId}")
    fun getSubtasksByTaskId(@PathVariable taskId: UUID): List<SubtaskResponse> {
        log.info("GET /subtasks/task/$taskId - Get subtasks by task ID")
        return subtaskApplication.getSubtasksByTaskId(taskId).map { it.toDTO() }
    }

    @Operation(
        summary = "Update subtask by ID",
        description = "Updates the subtask details for the specified subtask ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully updated subtask"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "403", description = "Forbidden – Not allowed to update this subtask"),
            ApiResponse(responseCode = "404", description = "Not Found – Subtask not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PutMapping("/{id}")
    fun updateSubtask(@PathVariable id: UUID, @RequestBody subtaskRequest: SubtaskRequest): SubtaskResponse {
        log.info("PUT /subtasks/$id - Update subtask")
        return subtaskApplication.updateSubtask(id, subtaskRequest.toModel()).toDTO()
    }

    @Operation(
        summary = "Delete subtask by ID",
        description = "Deletes the subtask with the specified ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Successfully deleted subtask"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "403", description = "Forbidden – Not allowed to delete this subtask"),
            ApiResponse(responseCode = "404", description = "Not Found – Subtask not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSubtask(@PathVariable id: UUID) {
        log.info("DELETE /subtasks/$id - Delete subtask")
        subtaskApplication.deleteSubtask(id)
    }

    @Operation(
        summary = "Toggle subtask completion status",
        description = "Toggles the completion status of the subtask with the specified ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully toggled subtask status"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "403", description = "Forbidden – Not allowed to update this subtask"),
            ApiResponse(responseCode = "404", description = "Not Found – Subtask not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PatchMapping("/{id}/toggle")
    fun toggleSubtaskStatus(@PathVariable id: UUID): SubtaskResponse {
        log.info("PATCH /subtasks/$id/toggle - Toggle subtask status")
        return subtaskApplication.toggleSubtaskStatus(id).toDTO()
    }
}