package dev.ecckea.agilepath.backend.domain.project.controller

import dev.ecckea.agilepath.backend.domain.project.application.ProjectMembershipApplication
import dev.ecckea.agilepath.backend.domain.project.dto.ProjectMemberRequest
import dev.ecckea.agilepath.backend.domain.project.dto.ProjectMemberResponse
import dev.ecckea.agilepath.backend.domain.project.model.ProjectRole
import dev.ecckea.agilepath.backend.domain.project.model.mapper.toDTO
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
@RequestMapping("/projects/{projectId}/members")
@Validated
@Tag(name = "Project Membership", description = "Manage project members and roles")
class ProjectMembershipController(
    private val projectMembershipApplication: ProjectMembershipApplication
) : Logged() {

    @Operation(
        summary = "Get members of a project",
        description = "Returns a list of users that are members of the specified project",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returned project members"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "403", description = "Forbidden – Not allowed to view members"),
            ApiResponse(responseCode = "404", description = "Project not found")
        ]
    )
    @GetMapping
    fun getProjectMembers(@PathVariable projectId: UUID): List<ProjectMemberResponse> {
        log.info("GET /projects/{}/members", projectId)
        return projectMembershipApplication.getMembers(projectId).map { it.toDTO() }
    }

    @Operation(
        summary = "Add a member to a project",
        description = "Adds a user to the specified project with the given role",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully added member to project"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "403", description = "Forbidden – Not allowed to add members"),
            ApiResponse(responseCode = "404", description = "Project not found")
        ]
    )
    @PostMapping
    fun addMember(
        @PathVariable projectId: UUID,
        @Valid @RequestBody request: ProjectMemberRequest
    ) {
        log.info("POST /projects/{}/members", projectId)
        projectMembershipApplication.addMember(projectId, request)
    }

    @Operation(
        summary = "Update a member's role in a project",
        description = "Updates the role of a user in the specified project",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully updated member's role"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "403", description = "Forbidden – Not allowed to update member's role"),
            ApiResponse(responseCode = "404", description = "Project not found")
        ]
    )
    @PutMapping("/{userId}")
    fun updateMemberRole(
        @PathVariable projectId: UUID,
        @PathVariable userId: String,
        @RequestParam role: ProjectRole
    ) {
        log.info("PUT /projects/{}/members/{} - role={}", projectId, userId, role)
        projectMembershipApplication.updateMemberRole(projectId, userId, role)
    }

    @Operation(
        summary = "Remove a member from a project",
        description = "Removes a user from the specified project",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully removed member from project"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "403", description = "Forbidden – Not allowed to remove members"),
            ApiResponse(responseCode = "404", description = "Project not found")
        ]
    )
    @DeleteMapping("/{userId}")
    fun removeMember(@PathVariable projectId: UUID, @PathVariable userId: String) {
        log.info("DELETE /projects/{}/members/{}", projectId, userId)
        projectMembershipApplication.removeMember(projectId, userId)
    }
}
