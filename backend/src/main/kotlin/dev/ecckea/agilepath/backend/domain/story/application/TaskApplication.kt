package dev.ecckea.agilepath.backend.domain.story.application

import dev.ecckea.agilepath.backend.domain.column.service.SprintColumnService
import dev.ecckea.agilepath.backend.domain.sprint.service.SprintService
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
    private val subtaskService: SubtaskService,
) : Logged() {

    fun createTask(newTask: NewTask): Task {
        log.info("Creating task with title: {}", newTask.title)

        val created = taskService.createTask(newTask)

        newTask.assigneeIds.forEach { taskAssigneeService.addAssignee(created.id, it) }

        val assignees = newTask.assigneeIds.map { userService.getById(it) }

        return created.copy(assignees = assignees)
    }

    fun getTask(id: UUID): Task {
        log.info("Getting task with id: {}", id)

        val task = taskService.getTask(id)
        val subtasks = subtaskService.getSubtasksByTaskId(id)
        val comments = commentService.getCommentsByTaskId(id)
        val assigneeIds = taskAssigneeService.getAssignees(id)
        val assignees = assigneeIds.map { userService.getById(it) }

        return task.copy(
            subtasks = subtasks.map { it },
            comments = comments.map { it },
            assignees = assignees
        )
    }

    fun getSprintColumnTasks(sprintColumnId: UUID): List<Task> {
        log.info("Getting tasks for sprint column with id: {}", sprintColumnId)
        return taskService.getTasksBySprintColumn(sprintColumnId)
    }

    fun updateTask(id: UUID, newTask: NewTask): Task {
        log.info("Updating task with id: {}", id)

        val updated = taskService.updateTask(id, newTask, currentUser().id)
        val currentAssignees = taskAssigneeService.getAssignees(id)
        val assigneesToRemove = currentAssignees.filter { it !in newTask.assigneeIds }
        assigneesToRemove.forEach { assigneeId ->
            taskAssigneeService.removeAssignee(id, assigneeId)
        }

        val assigneesToAdd = newTask.assigneeIds.filter { it !in currentAssignees }
        assigneesToAdd.forEach { assigneeId ->
            taskAssigneeService.addAssignee(id, assigneeId)
        }

        val assignees = newTask.assigneeIds.map { userService.getById(it) }

        return updated.copy(assignees = assignees)
    }

    fun deleteTask(id: UUID) {
        log.info("Deleting task with id: {}", id)
        taskService.deleteTask(id)
    }
}