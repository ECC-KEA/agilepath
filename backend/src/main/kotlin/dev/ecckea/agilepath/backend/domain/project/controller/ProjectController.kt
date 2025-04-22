package dev.ecckea.agilepath.backend.domain.project.controller

import dev.ecckea.agilepath.backend.domain.project.application.ProjectApplication
import dev.ecckea.agilepath.backend.domain.project.dto.ProjectRequest
import dev.ecckea.agilepath.backend.domain.project.dto.ProjectResponse
import dev.ecckea.agilepath.backend.domain.project.dto.toModel
import dev.ecckea.agilepath.backend.domain.project.model.toDTO
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.security.currentUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/project")
@Tag(name = "Project", description = "Endpoints related to project management")
class ProjectController(
    private val projectApplication: ProjectApplication
) : Logged() {

    @Operation(
        summary = "Get project by ID",
        description = "Returns the project details for the specified project ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returned project details"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to access this project"
            ),
            ApiResponse(responseCode = "404", description = "Not Found – Project with specified ID does not exist"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/{id}")
    fun getProject(@PathVariable id: UUID): ProjectResponse {
        log.info("GET /project/{id} - Get project")
        return projectApplication.getProject(id).toDTO()
    }

    @Operation(
        summary = "Create new project",
        description = "Creates a new project with the current authenticated user as owner",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully created project"),
            ApiResponse(responseCode = "400", description = "Bad Request – Invalid project data"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to create projects"
            ),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PostMapping
    fun createProject(@RequestBody project: ProjectRequest): ProjectResponse {
        log.info("POST /project/ - Create project")
        return projectApplication.createProject(project.toModel(currentUser().id)).toDTO()
    }

    @Operation(
        summary = "Delete project",
        description = "Permanently deletes the specified project",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Successfully deleted project"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to delete this project"
            ),
            ApiResponse(responseCode = "404", description = "Not Found – Project with specified ID does not exist"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @DeleteMapping("/{id}")
    fun deleteProject(@PathVariable id: UUID) {
        log.info("DELETE /project/{id} - Delete project")
        projectApplication.deleteProject(id)
    }

    @Operation(
        summary = "Update project",
        description = "Updates the specified project with new information",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully updated project"),
            ApiResponse(responseCode = "400", description = "Bad Request – Invalid project data"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to update this project"
            ),
            ApiResponse(responseCode = "404", description = "Not Found – Project with specified ID does not exist"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PutMapping("/{id}")
    fun updateProject(@PathVariable id: UUID, @RequestBody project: ProjectRequest): ProjectResponse {
        log.info("PUT /project/{id} - Update project")
        return projectApplication.updateProject(id, project.toModel(currentUser().id)).toDTO()
    }
}