package dev.ecckea.agilepath.backend.domain.story.application

import dev.ecckea.agilepath.backend.domain.column.model.SprintColumnStatus
import dev.ecckea.agilepath.backend.domain.column.service.SprintColumnService
import dev.ecckea.agilepath.backend.domain.sprint.model.SprintEventType
import dev.ecckea.agilepath.backend.domain.story.model.*
import dev.ecckea.agilepath.backend.domain.story.service.CommentService
import dev.ecckea.agilepath.backend.domain.story.service.SubtaskService
import dev.ecckea.agilepath.backend.domain.story.service.TaskAssigneeService
import dev.ecckea.agilepath.backend.domain.story.service.TaskService
import dev.ecckea.agilepath.backend.domain.user.service.UserService
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.logging.events.SprintEventLogger
import dev.ecckea.agilepath.backend.shared.logging.events.StoryEventLogger
import dev.ecckea.agilepath.backend.shared.logging.events.TaskEventLogger
import dev.ecckea.agilepath.backend.shared.security.currentUser
import dev.ecckea.agilepath.backend.shared.utils.ChangeDetector
import org.springframework.stereotype.Service
import java.util.*

@Service
class TaskApplication(
    private val sprintColumnService: SprintColumnService,
    private val taskService: TaskService,
    private val userService: UserService,
    private val taskAssigneeService: TaskAssigneeService,
    private val commentService: CommentService,
    private val subtaskService: SubtaskService,
    private val sprintEventLogger: SprintEventLogger,
    private val storyEventLogger: StoryEventLogger,
    private val taskEventLogger: TaskEventLogger
) : Logged() {

    fun createTask(newTask: NewTask): Task {
        log.info("Creating task with title: {}", newTask.title)
        val user = userService.get(currentUser())
        val created = taskService.createTask(newTask)

        newTask.assigneeIds.forEach { taskAssigneeService.addAssignee(created.id, it) }

        val assignees = newTask.assigneeIds.map { userService.getById(it) }

        sprintEventLogger.logEvent(
            entityId = created.id,
            eventType = SprintEventType.TASK_ADDED,
            triggeredBy = user,
            oldValue = null,
            newValue = created.title
        )

        storyEventLogger.logEvent(
            entityId = created.id,
            eventType = StoryEventType.TASK_ADDED,
            triggeredBy = user,
            oldValue = null,
            newValue = created.title,
        )

        taskEventLogger.logEvent(
            entityId = created.id,
            eventType = TaskEventType.CREATED,
            triggeredBy = user,
            oldValue = null,
            newValue = created.title
        )

        if (assignees.isNotEmpty()) {
            taskEventLogger.logEvent(
                entityId = created.id,
                eventType = TaskEventType.ASSIGNED,
                triggeredBy = user,
                oldValue = null,
                newValue = assignees.joinToString(", ") { it.fullName.toString() }
            )
        }

        if (newTask.estimatePoints != null || newTask.estimateTshirt != null) {
            taskEventLogger.logEvent(
                entityId = created.id,
                eventType = TaskEventType.ESTIMATED,
                triggeredBy = user,
                oldValue = null,
                newValue = newTask.estimatePoints.toString()
            )
        }

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

        val oldTask = taskService.getTask(id)
        val updatedTask = taskService.updateTask(id, newTask, currentUser().id)

        handleAndLogAssigneeChangeEvents(id, oldTask, newTask)
        logTaskPropertyChangeEvents(id, oldTask, updatedTask)

        val assignees = newTask.assigneeIds.map { userService.getById(it) }
        return updatedTask.copy(assignees = assignees)
    }

    fun deleteTask(id: UUID) {
        log.info("Deleting task with id: {}", id)
        sprintEventLogger.logEvent(
            entityId = id,
            eventType = SprintEventType.TASK_REMOVED,
            triggeredBy = userService.get(currentUser()),
            oldValue = null,
            newValue = null
        )

        storyEventLogger.logEvent(
            entityId = id,
            eventType = StoryEventType.TASK_REMOVED,
            triggeredBy = userService.get(currentUser()),
            oldValue = null,
            newValue = null
        )

        taskEventLogger.logEvent(
            entityId = id,
            eventType = TaskEventType.DELETED,
            triggeredBy = userService.get(currentUser()),
            oldValue = null,
            newValue = null
        )
        taskService.deleteTask(id)
    }

    private fun handleAndLogAssigneeChangeEvents(id: UUID, oldTask: Task, newTask: NewTask) {
        val currentAssignees = taskAssigneeService.getAssignees(id)
        val assigneesToRemove = currentAssignees.filter { it !in newTask.assigneeIds }
        val assigneesToAdd = newTask.assigneeIds.filter { it !in currentAssignees }

        // Handle assignee changes
        assigneesToRemove.forEach { assigneeId ->
            taskAssigneeService.removeAssignee(id, assigneeId)
        }
        assigneesToAdd.forEach { assigneeId ->
            taskAssigneeService.addAssignee(id, assigneeId)
        }

        // Log assignee change events
        val newAssignees = newTask.assigneeIds.map { userService.getById(it) }
        val user = userService.get(currentUser())

        when {
            newAssignees.isNotEmpty() && oldTask.assignees.isNullOrEmpty() -> {
                taskEventLogger.logEvent(
                    entityId = id,
                    eventType = TaskEventType.ASSIGNED,
                    triggeredBy = user,
                    oldValue = null,
                    newValue = newAssignees.joinToString(", ") { it.fullName.toString() }
                )
            }

            oldTask.assignees?.isNotEmpty() == true && newAssignees.isEmpty() -> {
                taskEventLogger.logEvent(
                    entityId = id,
                    eventType = TaskEventType.UNASSIGNED,
                    triggeredBy = user,
                    oldValue = oldTask.assignees.joinToString(", ") { it.fullName.toString() },
                    newValue = null
                )
            }

            assigneesToAdd.isNotEmpty() || assigneesToRemove.isNotEmpty() -> {
                taskEventLogger.logEvent(
                    entityId = id,
                    eventType = TaskEventType.REASSIGNED,
                    triggeredBy = user,
                    oldValue = oldTask.assignees?.joinToString(", ") { it.fullName.toString() },
                    newValue = newAssignees.joinToString(", ") { it.fullName.toString() }
                )
            }
        }
    }

    private fun logTaskPropertyChangeEvents(id: UUID, oldTask: Task, updatedTask: Task) {
        val changeResult = ChangeDetector.detectChanges(
            old = oldTask,
            new = updatedTask,
            excludedProperties = setOf("id", "createdAt", "updatedAt", "assignees")
        )

        if (!changeResult.hasChanges()) return

        val user = userService.get(currentUser())

        // Log changes for each property
        changeResult.changes.forEach { propertyChange ->
            when (propertyChange.propertyName) {
                "title" -> {
                    taskEventLogger.logEvent(
                        entityId = id,
                        eventType = TaskEventType.TITLE_CHANGED,
                        triggeredBy = user,
                        oldValue = propertyChange.oldValue?.toString(),
                        newValue = propertyChange.newValue?.toString()
                    )
                }

                "description" -> {
                    taskEventLogger.logEvent(
                        entityId = id,
                        eventType = TaskEventType.DESCRIPTION_CHANGED,
                        triggeredBy = user,
                        oldValue = propertyChange.oldValue?.toString(),
                        newValue = propertyChange.newValue?.toString()
                    )
                }

                "sprintColumnId" -> {
                    taskEventLogger.logEvent(
                        entityId = id,
                        eventType = TaskEventType.SPRINT_COLUMN_CHANGED,
                        triggeredBy = user,
                        oldValue = propertyChange.oldValue?.toString(),
                        newValue = propertyChange.newValue?.toString()
                    )
                }

                "estimatePoints" -> {
                    taskEventLogger.logEvent(
                        entityId = id,
                        eventType = TaskEventType.ESTIMATE_CHANGED,
                        triggeredBy = user,
                        oldValue = propertyChange.oldValue?.toString(),
                        newValue = propertyChange.newValue?.toString()
                    )
                }

                "estimateTshirt" -> {
                    taskEventLogger.logEvent(
                        entityId = id,
                        eventType = TaskEventType.ESTIMATE_CHANGED,
                        triggeredBy = user,
                        oldValue = (propertyChange.oldValue as? TshirtEstimate)?.name,
                        newValue = (propertyChange.newValue as? TshirtEstimate)?.name
                    )
                }
            }
            if (propertyChange.propertyName == "sprintColumnId") {
                val column = sprintColumnService.getSprintColumn(updatedTask.sprintColumnId)
                val sprintId = column.sprintId
                val columns = sprintColumnService.getSprintColumns(sprintId)

                // if the task was the first task in a column with SprintColumnStatus.IN_PROGRESS, log SprintEventType.STARTED
                if (columns.any { column -> column.id == updatedTask.sprintColumnId && column.status == SprintColumnStatus.IN_PROGRESS }) {
                    sprintEventLogger.logEvent(
                        entityId = sprintId,
                        eventType = SprintEventType.STARTED,
                        triggeredBy = user,
                        oldValue = null,
                        newValue = "Task ${updatedTask.title} moved to In Progress"
                    )
                }

                // if the task was moved to a column with SprintColumnStatus.IN_PROGRESS for the first time, log SprintEventType.STARTED
                if (columns.any { column -> column.id == updatedTask.sprintColumnId && column.status == SprintColumnStatus.IN_PROGRESS }) {
                    // check if this is the first time this task is being moved to a column with SprintColumnStatus.IN_PROGRESS
                    val wasInProgressBefore = oldTask.sprintColumnId.let {
                        sprintColumnService.getSprintColumn(it).status == SprintColumnStatus.IN_PROGRESS
                    }
                    if (!wasInProgressBefore) {
                        taskEventLogger.logEvent(
                            entityId = id,
                            eventType = TaskEventType.STARTED,
                            triggeredBy = user,
                            oldValue = null,
                            newValue = "Task ${updatedTask.title} moved to In Progress"
                        )
                    }
                }

                // if the task was moved to a column with SprintColumnStatus.DONE, log SprintEventType.COMPLETED
                if (columns.any { column -> column.id == updatedTask.sprintColumnId && column.status == SprintColumnStatus.DONE }) {
                    taskEventLogger.logEvent(
                        entityId = id,
                        eventType = TaskEventType.COMPLETED,
                        triggeredBy = user,
                        oldValue = null,
                        newValue = "Task ${updatedTask.title} moved to Done"
                    )
                }

                // if a task was moved from a column with SprintColumnStatus.DONE to a column with SprintColumnStatus.IN_PROGRESS, log SprintEventType.REOPENED
                if (columns.any { column -> column.id == updatedTask.sprintColumnId && column.status == SprintColumnStatus.IN_PROGRESS } &&
                    oldTask.sprintColumnId.let {
                        sprintColumnService.getSprintColumn(it).status == SprintColumnStatus.DONE
                    }) {
                    taskEventLogger.logEvent(
                        entityId = id,
                        eventType = TaskEventType.REOPENED,
                        triggeredBy = user,
                        oldValue = "Task ${oldTask.title} moved from Done",
                        newValue = "Task ${updatedTask.title} moved to In Progress"
                    )
                }
            }
        }
    }

}