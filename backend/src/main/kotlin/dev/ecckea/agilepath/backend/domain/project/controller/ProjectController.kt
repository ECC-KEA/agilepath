package dev.ecckea.agilepath.backend.domain.project.controller

import dev.ecckea.agilepath.backend.domain.project.application.ProjectApplication
import dev.ecckea.agilepath.backend.domain.project.application.ProjectMembershipApplication
import dev.ecckea.agilepath.backend.domain.project.dto.ProjectRequest
import dev.ecckea.agilepath.backend.domain.project.dto.ProjectResponse
import dev.ecckea.agilepath.backend.domain.project.model.mapper.toDTO
import dev.ecckea.agilepath.backend.domain.project.model.mapper.toModel
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.security.currentUser
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
@RequestMapping("/projects")
@Validated
@Tag(name = "Project", description = "Endpoints related to project management")
class ProjectController(
    private val projectApplication: ProjectApplication,
    private val projectMembershipApplication: ProjectMembershipApplication
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
        log.info("GET /projects/{} - Get project", id)
        return projectApplication.getProject(id).toDTO()
    }

    @Operation(
        summary = "Get projects for current user",
        description = "Returns a list of project details for the user that is currently authenticated",
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
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping
    fun getProjects(): List<ProjectResponse> {
        log.info("GET /projects/ - Get projects")
        return projectApplication.getProjects().map { it.toDTO() }
    }

    @Operation(
        summary = "Get projects for a specific user with a supplied id",
        description = "Returns a list of project details for the user with the supplied id",
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
            ApiResponse(responseCode = "404", description = "Not Found – User with specified ID does not exist"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/users/{userId}")
    fun getProjectsByUserId(@PathVariable userId: String): List<ProjectResponse> {
        log.info("GET /projects/{} - Get projects by userId", userId)
        return projectMembershipApplication.getProjectsForUser(userId).map { it.toDTO() }
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
    fun createProject(@Valid @RequestBody project: ProjectRequest): ProjectResponse {
        log.info("POST /projects/ - Create project")
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
        log.info("DELETE /projects/{} - Delete project", id)
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
    fun updateProject(@PathVariable id: UUID, @Valid @RequestBody project: ProjectRequest): ProjectResponse {
        log.info("PUT /projects/{} - Update project", id)
        return projectApplication.updateProject(id, project.toModel(currentUser().id)).toDTO()
    }
}