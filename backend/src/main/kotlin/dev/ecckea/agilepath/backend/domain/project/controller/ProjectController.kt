package dev.ecckea.agilepath.backend.domain.project.controller

import dev.ecckea.agilepath.backend.domain.project.application.ProjectApplication
import dev.ecckea.agilepath.backend.domain.project.dto.ProjectResponse
import dev.ecckea.agilepath.backend.domain.project.dto.toModel
import dev.ecckea.agilepath.backend.domain.project.model.toDTO
import dev.ecckea.agilepath.backend.shared.logging.Logged
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/projects")
@Tag(name = "Project", description = "Endpoints related to project management")
class ProjectController(
    private val projectApplication: ProjectApplication
) : Logged() {

    @GetMapping("/{id}")
    suspend fun getProject(@PathVariable id: UUID): ProjectResponse {
        log.info("GET /project/{id} - Get project")
        return projectApplication.getProject(id).toDTO()
    }

    @PostMapping
    suspend fun createProject(@RequestBody project: ProjectResponse): ProjectResponse {
        log.info("POST /project/ - Create project")
        return projectApplication.createProject(project.toModel()).toDTO()
    }

    @DeleteMapping("/{id}")
    suspend fun deleteProject(@PathVariable id: String) {
        log.info("DELETE /project/{id} - Delete project")
        projectApplication.deleteProject(id)
    }

    @PutMapping("/{id}")
    suspend fun updateProject(@PathVariable id: String, @RequestBody project: ProjectResponse): ProjectResponse {
        log.info("PUT /project/{id} - Update project")
        return projectApplication.updateProject(id, project.toModel()).toDTO()
    }


}