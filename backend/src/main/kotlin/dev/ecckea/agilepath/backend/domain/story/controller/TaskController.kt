package dev.ecckea.agilepath.backend.domain.story.controller

import dev.ecckea.agilepath.backend.domain.story.application.TaskApplication
import dev.ecckea.agilepath.backend.domain.story.dto.TaskRequest
import dev.ecckea.agilepath.backend.domain.story.dto.TaskResponse
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toDTO
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toModel
import dev.ecckea.agilepath.backend.shared.logging.Logged
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.collections.List

@RestController
@Validated
@Tag(name = "Tasks", description = "Endpoints related to Task management")
class TaskController(
    private val taskApplication: TaskApplication
) : Logged() {

    @Operation(
        summary = "Create a new task",
        description = "Creates a new task with the provided details",
        security = [SecurityRequirement(name = "bearerAuth")]
    )

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully created task"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to create a task"
            ),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )

    @PostMapping("/tasks")
    fun createTask(@Valid @RequestBody taskRequest: TaskRequest): TaskResponse {
        log.info("POST /tasks - Create task")
        return taskApplication.createTask(taskRequest.toModel()).toDTO()
    }

    @Operation(
        summary = "Get task by ID",
        description = "Returns the task details for the specified task ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returned task details"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to access this task"
            ),
            ApiResponse(responseCode = "404", description = "Not Found – Task with specified ID does not exist"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/tasks/{id}")
    fun getTask(@PathVariable id: UUID): TaskResponse {
        log.info("GET /tasks/$id - Get task by ID")
        return taskApplication.getTask(id).toDTO()
    }

    @Operation(
        summary = "Get tasks by sprint column ID",
        description = "Returns the tasks with details for the specified sprint column ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returned tasks"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to access this task"
            ),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/sprint-columns/{sprintColumnID}/tasks")
    fun getSprintColumnTasks(@PathVariable sprintColumnID: UUID): List<TaskResponse> {
        log.info("GET /sprint-columns/$sprintColumnID/tasks - Get tasks by sprint column ID")
        return taskApplication.getSprintColumnTasks(sprintColumnID).map { it.toDTO() }
    }

    @Operation(
        summary = "Update task by ID",
        description = "Updates the task details for the specified task ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully updated task"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to update this task"
            ),
            ApiResponse(responseCode = "404", description = "Not Found – Task with specified ID does not exist"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PutMapping("/tasks/{id}")
    fun updateTask(@PathVariable id: UUID, @Valid @RequestBody taskRequest: TaskRequest): TaskResponse {
        log.info("PUT /tasks/$id - Update task")
        return taskApplication.updateTask(id, taskRequest.toModel()).toDTO()
    }

    @Operation(
        summary = "Delete task by ID",
        description = "Deletes the task with the specified ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Successfully deleted task"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to delete this task"
            ),
            ApiResponse(responseCode = "404", description = "Not Found – Task with specified ID does not exist"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTask(@PathVariable id: UUID) {
        log.info("DELETE /tasks/$id - Delete task")
        taskApplication.deleteTask(id)
    }
}