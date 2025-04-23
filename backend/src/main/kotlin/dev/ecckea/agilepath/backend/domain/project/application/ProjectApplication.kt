package dev.ecckea.agilepath.backend.domain.project.application

import dev.ecckea.agilepath.backend.domain.project.model.NewProject
import dev.ecckea.agilepath.backend.domain.project.model.Project
import dev.ecckea.agilepath.backend.domain.project.service.ProjectService
import dev.ecckea.agilepath.backend.shared.security.UserPrincipal
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.List

@Service
class ProjectApplication (
    private val projectService: ProjectService,
) {
    fun getProject(id: UUID): Project {
        return projectService.getProject(id)
    }

    fun getProjects(userPrincipal: UserPrincipal): List<Project> {
        return projectService.getProjects(userPrincipal)
    }

    fun createProject(project: NewProject): Project {
        return projectService.createProject(project)
    }

    fun deleteProject(id: UUID) {
        projectService.deleteProject(id)
    }

    fun updateProject(id: UUID, project: NewProject): Project {
        return projectService.updateProject(id, project)
    }
}
