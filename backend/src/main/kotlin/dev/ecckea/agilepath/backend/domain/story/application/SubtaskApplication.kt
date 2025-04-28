package dev.ecckea.agilepath.backend.domain.story.application

import dev.ecckea.agilepath.backend.domain.story.model.NewSubtask
import dev.ecckea.agilepath.backend.domain.story.model.Subtask
import dev.ecckea.agilepath.backend.domain.story.service.SubtaskService
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.security.currentUser
import org.springframework.stereotype.Service
import java.util.*

@Service
class SubtaskApplication(
    private val subtaskService: SubtaskService
) : Logged() {

    fun createSubtask(newSubtask: NewSubtask): Subtask {
        log.info("Creating subtask for task: {}", newSubtask.taskId)
        return subtaskService.createSubtask(newSubtask)
    }

    fun getSubtask(id: UUID): Subtask {
        log.info("Getting subtask with id: {}", id)
        return subtaskService.getSubtask(id)
    }

    fun getSubtasksByTaskId(taskId: UUID): List<Subtask> {
        log.info("Getting subtasks for task: {}", taskId)
        return subtaskService.getSubtasksByTaskId(taskId)
    }

    fun updateSubtask(id: UUID, newSubtask: NewSubtask): Subtask {
        log.info("Updating subtask with id: {}", id)
        return subtaskService.updateSubtask(id, newSubtask, currentUser().id)
    }

    fun deleteSubtask(id: UUID) {
        log.info("Deleting subtask with id: {}", id)
        subtaskService.deleteSubtask(id)
    }

    fun toggleSubtaskStatus(id: UUID): Subtask {
        log.info("Toggling status for subtask with id: {}", id)
        return subtaskService.toggleSubtaskStatus(id, currentUser().id)
    }
}