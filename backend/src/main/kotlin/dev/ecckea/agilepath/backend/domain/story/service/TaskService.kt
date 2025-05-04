package dev.ecckea.agilepath.backend.domain.story.service

import dev.ecckea.agilepath.backend.domain.story.model.NewTask
import dev.ecckea.agilepath.backend.domain.story.model.Task
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toEntity
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toModel
import dev.ecckea.agilepath.backend.domain.story.model.mapper.updatedWith
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TaskService(
    private val ctx: RepositoryContext,
    private val cacheManager: CacheManager
) : Logged() {

    @Transactional
    @CacheEvict(value = ["tasksByStory"], key = "#newTask.storyId")
    fun createTask(newTask: NewTask): Task {
        log.info("Creating new task: {}", newTask)

        val storyExists = ctx.story.existsById(newTask.storyId)
        require(storyExists) { throw ResourceNotFoundException("Story with id ${newTask.storyId} not found") }

        val columnExists = ctx.sprintColumn.existsById(newTask.sprintColumnId)
        require(columnExists) { throw ResourceNotFoundException("Sprint column with id ${newTask.sprintColumnId} not found") }

        val taskEntity = newTask.toEntity(ctx)
        return ctx.task.save(taskEntity).toModel()
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["tasks"], key = "#id")
    fun getTask(id: UUID): Task {
        return ctx.task.findOneById(id)?.toModel()
            ?: throw ResourceNotFoundException("Task with id $id not found")
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["tasksByStory"], key = "#storyId")
    fun getTasksByStoryId(storyId: UUID): List<Task> {
        return ctx.task.findByStoryId(storyId).map { it.toModel() }
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict(value = ["tasks"], key = "#id"),
            CacheEvict(value = ["tasksByStory"], key = "#newTask.storyId")
        ]
    )
    fun updateTask(id: UUID, newTask: NewTask, userId: String): Task {
        log.info("Updating task with id: $id")
        val taskEntity = ctx.task.findOneById(id)
            ?: throw ResourceNotFoundException("Task with id $id not found")

        require(taskEntity.story.id == newTask.storyId) {
            "Story ID cannot be changed during update"
        }
        require(taskEntity.sprintColumn.id == newTask.sprintColumnId) {
            "Sprint column ID cannot be changed during update"
        }

        val updatedEntity = taskEntity.updatedWith(newTask, userId, ctx)
        return ctx.task.save(updatedEntity).toModel()
    }

    @Transactional
    @CacheEvict(value = ["tasks"], key = "#id")
    fun deleteTask(id: UUID) {
        val task = ctx.task.findOneById(id)
            ?: throw ResourceNotFoundException("Task with id $id not found")

        val storyId = task.story.id
        if (storyId != null) {
            cacheManager.getCache("tasksByStory")?.evict(storyId)
        }

        ctx.task.delete(task)
    }
}