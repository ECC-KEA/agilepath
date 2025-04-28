package dev.ecckea.agilepath.backend.domain.story.service

import dev.ecckea.agilepath.backend.domain.story.repository.TaskAssigneeRepository
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.stereotype.Service
import java.util.*

@Service
class TaskAssigneeService(
    private val taskAssigneeRepository: TaskAssigneeRepository,
) : Logged() {

    fun addAssignee(taskId: UUID, userId: String) {
        taskAssigneeRepository.addAssignee(taskId, userId)
    }

    fun removeAssignee(taskId: UUID, userId: String) {
        taskAssigneeRepository.removeAssignee(taskId, userId)
    }

    fun getAssignees(taskId: UUID): List<String> {
        return taskAssigneeRepository.getAssignees(taskId)
    }

    fun getTasksByAssignee(userId: String): List<UUID> {
        return taskAssigneeRepository.getTasksByAssignee(userId)
    }
}