package dev.ecckea.agilepath.backend.domain.project.application

import dev.ecckea.agilepath.backend.domain.project.model.NewProject
import dev.ecckea.agilepath.backend.domain.project.model.Project
import dev.ecckea.agilepath.backend.domain.project.service.ProjectService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProjectApplication (
    private val projectService: ProjectService,
) {
    fun getProject(id: UUID): Project {
        return projectService.getProject(id)
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
