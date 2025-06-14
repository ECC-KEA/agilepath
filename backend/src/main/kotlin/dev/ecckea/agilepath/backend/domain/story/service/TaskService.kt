package dev.ecckea.agilepath.backend.domain.story.service

import dev.ecckea.agilepath.backend.domain.story.model.NewTask
import dev.ecckea.agilepath.backend.domain.story.model.Task
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toEntity
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toModel
import dev.ecckea.agilepath.backend.domain.user.model.mapper.toModel
import dev.ecckea.agilepath.backend.domain.story.model.mapper.updatedWith
import dev.ecckea.agilepath.backend.infrastructure.cache.*
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TaskService(
    private val ctx: RepositoryContext,
    private val cacheService: CacheService
) : Logged() {

    @Transactional
    fun createTask(newTask: NewTask): Task {
        log.info("Creating new task: {}", newTask)

        val storyExists = ctx.story.existsById(newTask.storyId)
        require(storyExists) { throw ResourceNotFoundException("Story with id ${newTask.storyId} not found") }

        val columnExists = ctx.sprintColumn.existsById(newTask.sprintColumnId)
        require(columnExists) { throw ResourceNotFoundException("Sprint column with id ${newTask.sprintColumnId} not found") }

        // Invalidate related caches
        cacheService.invalidateStory(newTask.storyId)
        cacheService.invalidateStoryTasks(newTask.storyId)
        cacheService.invalidateSprintColumnTasks(newTask.sprintColumnId)

        val taskEntity = newTask.toEntity(ctx)
        return ctx.task.save(taskEntity).toModel(emptyList(), emptyList(), emptyList())
    }

    @Transactional(readOnly = true)
    fun getTask(id: UUID): Task {
        log.info("Fetching task with id: $id")
        // Check if the task is in the cache
        cacheService.getTask(id)?.let { return it }

        // If not in cache, get from database and cache it
        return getFromDbAndCache(id)
    }

    @Transactional(readOnly = true)
    fun getTasksBySprintColumn(sprintColumnID: UUID): List<Task> {
        log.info("Fetching tasks for sprint column $sprintColumnID")

        // Check if the tasks are in the cache
        cacheService.getSprintColumnTasks(sprintColumnID)?.let { return it }

        // If not in cache, get from database and cache it
        val sprintColumnEntity = ctx.sprintColumn.findOneById(sprintColumnID)
            ?: throw ResourceNotFoundException("Sprint column with id $sprintColumnID not found")

        val tasks = ctx.task.findBySprintColumn(sprintColumnEntity).map { 
            val taskId = it.id!!
            val cmts = ctx.comment.findByTaskId(taskId).map { it.toModel() }
            val subtasks = ctx.subtask.findByTaskId(taskId).map { it.toModel() }
            val assignees = ctx.taskAssinee.getAssignees(taskId).map { ctx.user.findOneById(it)?.toModel() ?: throw ResourceNotFoundException("User with id $it not found") }
            it.toModel(cmts, subtasks, assignees)
        }
        cacheService.cacheSprintColumnTasks(sprintColumnID, tasks)
        return tasks
    }

    @Transactional(readOnly = true)
    fun getTasksByStoryId(storyId: UUID): List<Task> {
        log.info("Fetching tasks for story $storyId")

        // Check if the tasks are in the cache
        cacheService.getStoryTasks(storyId)?.let { return it }

        // If not in cache, get from database and cache it
        val tasks = ctx.task.findByStoryId(storyId).map { 
            val taskId = it.id!!
            val cmts = ctx.comment.findByTaskId(taskId).map { it.toModel() }
            val subtasks = ctx.subtask.findByTaskId(taskId).map { it.toModel() }
            val assignees = ctx.taskAssinee.getAssignees(taskId).map { ctx.user.findOneById(it)?.toModel() ?: throw ResourceNotFoundException("User with id $it not found") }
            it.toModel(cmts, subtasks, assignees)
        }
        cacheService.cacheStoryTasks(storyId, tasks)

        return tasks
    }

    @Transactional
    fun updateTask(id: UUID, newTask: NewTask, userId: String): Task {
        log.info("Updating task with id: $id")
        val taskEntity = ctx.task.findOneById(id)
            ?: throw ResourceNotFoundException("Task with id $id not found")

        require(taskEntity.story.id == newTask.storyId) {
            "Story ID cannot be changed during update"
        }

        val updatedEntity = taskEntity.updatedWith(newTask, userId, ctx)
        val savedEntity = ctx.task.save(updatedEntity)
        val cmts = ctx.comment.findByTaskId(id).map { it.toModel() }
        val subtasks = ctx.subtask.findByTaskId(id).map { it.toModel() }
        val assignees = ctx.taskAssinee.getAssignees(id).map { ctx.user.findOneById(it)?.toModel() ?: throw ResourceNotFoundException("User with id $it not found") }
        val task = savedEntity.toModel(cmts, subtasks, assignees)

        // Invalidate caches
        cacheService.invalidateTask(id)
        cacheService.invalidateStory(newTask.storyId)
        cacheService.invalidateStoryTasks(newTask.storyId)
        cacheService.invalidateSprintColumnTasks(newTask.sprintColumnId)

        return task
    }

    @Transactional
    fun deleteTask(id: UUID) {
        log.info("Deleting task with id: $id")

        val task = ctx.task.findOneById(id)
            ?: throw ResourceNotFoundException("Task with id $id not found")

        val storyId = task.story.id
        val sprintColumnId = task.sprintColumn.id

        // Invalidate caches
        cacheService.invalidateTask(id)
        if (storyId != null) {
            cacheService.invalidateStoryTasks(storyId)
            cacheService.invalidateStory(storyId)

        }
        if (sprintColumnId != null) {
            cacheService.invalidateSprintColumnTasks(sprintColumnId)
        }

        ctx.task.delete(task)
    }

    private fun getFromDbAndCache(id: UUID): Task {
        log.info("Fetching task $id from database")
        val cmts = ctx.comment.findByTaskId(id).map { it.toModel() }
        val subtasks = ctx.subtask.findByTaskId(id).map { it.toModel() }
        val assignees = ctx.taskAssinee.getAssignees(id).map { ctx.user.findOneById(it)?.toModel() ?: throw ResourceNotFoundException("User with id $it not found") }
        val task = ctx.task.findOneById(id)?.toModel(cmts, subtasks, assignees)
            ?: throw ResourceNotFoundException("Task with id $id not found")

        cacheService.cacheTask(task)
        return task
    }
}