package dev.ecckea.agilepath.backend.domain.project.application

import dev.ecckea.agilepath.backend.domain.project.model.Project
import dev.ecckea.agilepath.backend.domain.project.service.ProjectService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProjectApplication (
    private val projectService: ProjectService,
) {
    suspend fun getProject(id: UUID): Project {
        return projectService.getProject(id)
    }

    suspend fun createProject(project: Project): Project {
        return projectService.createProject(project)
    }

    suspend fun deleteProject(id: String) {
        projectService.deleteProject(id)
    }

    suspend fun updateProject(id: String, project: Project): Project {
        return projectService.updateProject(id, project)
    }
}
