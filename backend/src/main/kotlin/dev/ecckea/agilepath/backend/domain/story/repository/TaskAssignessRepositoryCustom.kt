package dev.ecckea.agilepath.backend.domain.story.repository

import java.util.*

interface TaskAssigneeRepositoryCustom {
    fun addAssignee(taskId: UUID, userId: String)
    fun removeAssignee(taskId: UUID, userId: String)
    fun getAssignees(taskId: UUID): List<String>
    fun getTasksByAssignee(userId: String): List<UUID>
}