package dev.ecckea.agilepath.backend.domain.project.application

import dev.ecckea.agilepath.backend.domain.project.dto.ProjectMemberRequest
import dev.ecckea.agilepath.backend.domain.project.model.Project
import dev.ecckea.agilepath.backend.domain.project.model.ProjectEventType
import dev.ecckea.agilepath.backend.domain.project.model.ProjectMember
import dev.ecckea.agilepath.backend.domain.project.model.ProjectRole
import dev.ecckea.agilepath.backend.domain.project.service.UserProjectService
import dev.ecckea.agilepath.backend.domain.user.service.UserService
import dev.ecckea.agilepath.backend.shared.logging.events.ProjectEventLogger
import dev.ecckea.agilepath.backend.shared.security.currentUser
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProjectMembershipApplication(
    private val userProjectService: UserProjectService,
    private val userService: UserService,
    private val eventLogger: ProjectEventLogger,
) {

    fun getMembers(projectId: UUID): List<ProjectMember> {
        return userProjectService.getUsersInProject(projectId)
    }

    fun addMember(projectId: UUID, request: ProjectMemberRequest) {
        val userId = request.userId
        val role = ProjectRole.fromString(request.role)
        userProjectService.addUserToProject(userId, projectId, role)

        val user = userService.get(currentUser())
        eventLogger.logEvent(
            entityId = projectId,
            eventType = ProjectEventType.TEAM_MEMBER_ADDED,
            triggeredBy = user,
            oldValue = null,
            newValue = "$userId as ${role.name}"
        )
    }

    fun updateMemberRole(projectId: UUID, userId: String, role: ProjectRole) {
        userProjectService.updateUserProjectRole(userId, projectId, role)
    }

    fun removeMember(projectId: UUID, userId: String) {
        userProjectService.removeUserFromProject(userId, projectId)

        val user = userService.get(currentUser())
        eventLogger.logEvent(
            entityId = projectId,
            eventType = ProjectEventType.TEAM_MEMBER_REMOVED,
            triggeredBy = user,
            oldValue = userId,
            newValue = null
        )
    }

    fun getProjectsForUser(userId: String): List<Project> {
        return userProjectService.getProjectsForUser(userId)
    }
}
