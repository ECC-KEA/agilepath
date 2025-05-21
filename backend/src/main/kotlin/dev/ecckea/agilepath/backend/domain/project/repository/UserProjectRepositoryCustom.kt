package dev.ecckea.agilepath.backend.domain.project.repository

import dev.ecckea.agilepath.backend.domain.project.model.ProjectRole
import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEntity
import dev.ecckea.agilepath.backend.domain.project.repository.entity.UserProjectEntity
import java.util.*

interface UserProjectRepositoryCustom {
    fun addUserToProject(userId: String, projectId: UUID, role: ProjectRole)
    fun updateUserProjectRole(userId: String, projectId: UUID, role: ProjectRole)
    fun removeUserFromProject(userId: String, projectId: UUID)
    fun getProjectsForUser(userId: String): List<ProjectEntity>
    fun findAllByProjectId(projectId: UUID): List<UserProjectEntity>
}