package dev.ecckea.agilepath.backend.domain.story.application

import dev.ecckea.agilepath.backend.domain.story.model.NewTask
import dev.ecckea.agilepath.backend.domain.story.model.Task
import dev.ecckea.agilepath.backend.domain.story.service.CommentService
import dev.ecckea.agilepath.backend.domain.story.service.SubtaskService
import dev.ecckea.agilepath.backend.domain.story.service.TaskAssigneeService
import dev.ecckea.agilepath.backend.domain.story.service.TaskService
import dev.ecckea.agilepath.backend.domain.user.service.UserService
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.security.currentUser
import org.springframework.stereotype.Service
import java.util.*

@Service
class TaskApplication(
    private val taskService: TaskService,
    private val userService: UserService,
    private val taskAssigneeService: TaskAssigneeService,
    private val commentService: CommentService,
    private val subtaskService: SubtaskService
) : Logged() {

    fun createTask(newTask: NewTask): Task {
        log.info("Creating task with title: {}", newTask.title)

        // Create the task
        val created = taskService.createTask(newTask)

        // Add assignees - use forEach for side effects
        newTask.assigneeIds.forEach { taskAssigneeService.addAssignee(created.id, it) }

        // Get assignees for response - use map for transformation
        val assignees = newTask.assigneeIds.map { userService.getById(it) }

        return created.copy(assignees = assignees)
    }

    fun getTask(id: UUID): Task {
        log.info("Getting task with id: {}", id)

        // Get the task and related entities
        val task = taskService.getTask(id)
        val subtasks = subtaskService.getSubtasksByTaskId(id)
        val comments = commentService.getCommentsByTaskId(id)
        val assigneeIds = taskAssigneeService.getAssignees(id)
        val assignees = assigneeIds.map { userService.getById(it) }

        // Use copy to create a new instance with additional data
        return task.copy(
            subtasks = subtasks.map { it },
            comments = comments.map { it },
            assignees = assignees
        )
    }

    fun updateTask(id: UUID, newTask: NewTask): Task {
        log.info("Updating task with id: {}", id)

        // Update the task entity
        val updated = taskService.updateTask(id, newTask, currentUser().id)

        // Update assignees
        val currentAssignees = taskAssigneeService.getAssignees(id)

        // Use filter+subtract for more idiomatic collection operations
        // Remove assignees that are no longer in the list
        val assigneesToRemove = currentAssignees.filter { it !in newTask.assigneeIds }
        assigneesToRemove.forEach { assigneeId ->
            taskAssigneeService.removeAssignee(id, assigneeId)
        }

        // Add new assignees
        val assigneesToAdd = newTask.assigneeIds.filter { it !in currentAssignees }
        assigneesToAdd.forEach { assigneeId ->
            taskAssigneeService.addAssignee(id, assigneeId)
        }

        // Retrieve updated assignees for response
        val assignees = newTask.assigneeIds.map { userService.getById(it) }

        return updated.copy(assignees = assignees)
    }

    fun deleteTask(id: UUID) {
        log.info("Deleting task with id: {}", id)
        taskService.deleteTask(id)
    }
}