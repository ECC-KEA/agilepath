package dev.ecckea.agilepath.backend.domain.project.application

import dev.ecckea.agilepath.backend.domain.project.model.Project
import dev.ecckea.agilepath.backend.domain.project.service.ProjectService
import dev.ecckea.agilepath.backend.domain.user.model.User
import dev.ecckea.agilepath.backend.domain.user.service.UserService
import dev.ecckea.agilepath.backend.shared.security.UserPrincipal
import org.springframework.stereotype.Service
import java.util.UUID

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
