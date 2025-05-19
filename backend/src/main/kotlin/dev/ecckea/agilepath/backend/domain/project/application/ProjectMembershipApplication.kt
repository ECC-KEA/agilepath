package dev.ecckea.agilepath.backend.domain.project.application

import dev.ecckea.agilepath.backend.domain.project.dto.ProjectMemberRequest
import dev.ecckea.agilepath.backend.domain.project.model.Project
import dev.ecckea.agilepath.backend.domain.project.model.ProjectMember
import dev.ecckea.agilepath.backend.domain.project.model.ProjectRole
import dev.ecckea.agilepath.backend.domain.project.service.UserProjectService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProjectMembershipApplication(
    private val userProjectService: UserProjectService
) {

    fun getMembers(projectId: UUID): List<ProjectMember> {
        return userProjectService.getUsersInProject(projectId)
    }

    fun addMember(projectId: UUID, request: ProjectMemberRequest) {
        val userId = request.userId
        val role = ProjectRole.fromString(request.role)
        userProjectService.addUserToProject(userId, projectId, role)
    }

    fun updateMemberRole(projectId: UUID, userId: String, role: ProjectRole) {
        userProjectService.updateUserProjectRole(userId, projectId, role)
    }

    fun removeMember(projectId: UUID, userId: String) {
        userProjectService.removeUserFromProject(userId, projectId)
    }

    fun getProjectsForUser(userId: String): List<Project> {
        return userProjectService.getProjectsForUser(userId)
    }
}
