package dev.ecckea.agilepath.backend.domain.project.application

import dev.ecckea.agilepath.backend.domain.project.model.NewProject
import dev.ecckea.agilepath.backend.domain.project.model.Project
import dev.ecckea.agilepath.backend.domain.project.model.ProjectEventType
import dev.ecckea.agilepath.backend.domain.project.model.ProjectRole
import dev.ecckea.agilepath.backend.domain.project.service.ProjectService
import dev.ecckea.agilepath.backend.domain.project.service.UserProjectService
import dev.ecckea.agilepath.backend.domain.user.service.UserService
import dev.ecckea.agilepath.backend.shared.logging.events.ProjectEventLogger
import dev.ecckea.agilepath.backend.shared.security.currentUser
import dev.ecckea.agilepath.backend.shared.utils.ChangeDetector
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProjectApplication(
    private val projectService: ProjectService,
    private val userService: UserService,
    private val userProjectService: UserProjectService,
    private val eventLogger: ProjectEventLogger
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
        val createdProject = projectService.createProject(project, user)
        userProjectService.addUserToProject(user.id, createdProject.id, ProjectRole.OWNER)

        eventLogger.logEvent(
            entityId = createdProject.id,
            eventType = ProjectEventType.CREATED,
            triggeredBy = user,
            oldValue = null,
            newValue = createdProject.name
        )

        return createdProject
    }

    fun deleteProject(id: UUID) {
        projectService.deleteProject(id)
    }

    fun updateProject(id: UUID, project: NewProject): Project {
        val user = userService.get(currentUser())
        val oldProject = projectService.getProject(id)
        val changes = ChangeDetector.detectChanges(oldProject, project)

        if (changes.hasChanges() && changes.contains("framework")) {
            eventLogger.logEvent(
                entityId = id,
                eventType = ProjectEventType.FRAMEWORK_CHANGED,
                triggeredBy = user,
                oldValue = oldProject.framework.name,
                newValue = project.framework.name
            )
        }

        return projectService.updateProject(id, project, user)
    }
}

