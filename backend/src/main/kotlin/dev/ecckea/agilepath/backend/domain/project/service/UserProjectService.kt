package dev.ecckea.agilepath.backend.domain.project.service

import dev.ecckea.agilepath.backend.domain.project.model.Project
import dev.ecckea.agilepath.backend.domain.project.model.ProjectMember
import dev.ecckea.agilepath.backend.domain.project.model.ProjectRole
import dev.ecckea.agilepath.backend.domain.project.model.mapper.toModel
import dev.ecckea.agilepath.backend.domain.user.model.mapper.toModel
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserProjectService(
    private val ctx: RepositoryContext,
) : Logged() {

    fun addUserToProject(userId: String, projectId: UUID, role: ProjectRole) {
        log.info("Adding user with id $userId to project with id $projectId with role $role")
        checkUserAndProjectExist(userId, projectId)

        ctx.userProject.addUserToProject(userId, projectId, role)
    }

    fun updateUserProjectRole(userId: String, projectId: UUID, role: ProjectRole) {
        log.info("Updating user with id $userId role in project with id $projectId to $role")
        checkUserAndProjectExist(userId, projectId)
        ctx.userProject.updateUserProjectRole(userId, projectId, role)
    }

    fun removeUserFromProject(userId: String, projectId: UUID) {
        log.info("Removing user with id $userId from project with id $projectId")
        checkUserAndProjectExist(userId, projectId)
        ctx.userProject.removeUserFromProject(userId, projectId)
    }

    fun getUsersInProject(projectId: UUID): List<ProjectMember> {
        log.info("Fetching users in project with id $projectId")
        checkProjectExists(projectId)

        return ctx.userProject.findAllByProjectId(projectId)
            .map { ProjectMember(user = it.user.toModel(), role = it.role) }
    }

    fun getProjectsForUser(userId: String): List<Project> {
        log.info("Fetching projects for user with id $userId")
        checkUserExists(userId)
        return ctx.userProject.getProjectsForUser(userId).map { it.toModel() }
    }

    private fun checkUserAndProjectExist(userId: String, projectId: UUID) {
        checkUserExists(userId)
        checkProjectExists(projectId)
    }

    private fun checkUserExists(userId: String) {
        val exists = ctx.user.existsById(userId)
        require(exists) { throw ResourceNotFoundException("User with id $userId not found") }
    }

    private fun checkProjectExists(projectId: UUID) {
        val exists = ctx.project.existsById(projectId)
        require(exists) { throw ResourceNotFoundException("Project with id $projectId not found") }
    }
}