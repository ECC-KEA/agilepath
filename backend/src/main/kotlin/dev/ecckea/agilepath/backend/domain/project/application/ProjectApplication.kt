package dev.ecckea.agilepath.backend.domain.project.application

import dev.ecckea.agilepath.backend.domain.project.model.NewProject
import dev.ecckea.agilepath.backend.domain.project.model.Project
import dev.ecckea.agilepath.backend.domain.project.service.ProjectService
import dev.ecckea.agilepath.backend.domain.user.service.UserService
import dev.ecckea.agilepath.backend.shared.security.currentUser
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProjectApplication(
    private val projectService: ProjectService,
    private val userService: UserService,
) {

    fun getProject(id: UUID): Project {
        return projectService.getProject(id)
    }

    fun getProjects(): List<Project> {
        val user = userService.get(currentUser())
        return projectService.getProjects(user)
    }

    fun createProject(project: NewProject): Project {
        val user = userService.get(currentUser())
        return projectService.createProject(project, user)
    }

    fun deleteProject(id: UUID) {
        projectService.deleteProject(id)
    }

    fun updateProject(id: UUID, project: NewProject): Project {
        val user = userService.get(currentUser())
        return projectService.updateProject(id, project, user)
    }
}

