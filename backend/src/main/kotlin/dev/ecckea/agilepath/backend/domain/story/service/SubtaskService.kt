package dev.ecckea.agilepath.backend.domain.story.service

import dev.ecckea.agilepath.backend.domain.story.model.NewSubtask
import dev.ecckea.agilepath.backend.domain.story.model.Subtask
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toEntity
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toModel
import dev.ecckea.agilepath.backend.domain.story.model.mapper.updatedWith
import dev.ecckea.agilepath.backend.infrastructure.cache.*
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SubtaskService(
    private val ctx: RepositoryContext,
    private val cacheService: CacheService
) : Logged() {

    @Transactional
    fun createSubtask(newSubtask: NewSubtask): Subtask {
        log.info("Creating new subtask: {}", newSubtask.title)

        val taskExists = ctx.task.existsById(newSubtask.taskId)
        require(taskExists) { throw ResourceNotFoundException("Task with id ${newSubtask.taskId} not found") }

        val subtaskEntity = newSubtask.toEntity(ctx)
        val savedEntity = ctx.subtask.save(subtaskEntity)
        val subtask = savedEntity.toModel()

        // Invalidate task subtasks cache
        cacheService.invalidateTaskSubtasks(newSubtask.taskId)

        return subtask
    }

    @Transactional(readOnly = true)
    fun getSubtask(id: UUID): Subtask {
        log.info("Fetching subtask with id: $id")

        // Check if the subtask is in the cache
        cacheService.getSubtask(id)?.let { return it }

        // If not in cache, get from database and cache it
        return getFromDbAndCache(id)
    }

    @Transactional(readOnly = true)
    fun getSubtasksByTaskId(taskId: UUID): List<Subtask> {
        log.info("Fetching subtasks for task with id: $taskId")

        // Check if the subtasks are in the cache
        cacheService.getTaskSubtasks(taskId)?.let { return it }

        // If not in cache, get from database and cache it
        val subtasks = ctx.subtask.findByTaskId(taskId).map { it.toModel() }

        cacheService.cacheTaskSubtasks(taskId, subtasks)
        return subtasks
    }

    @Transactional
    fun updateSubtask(id: UUID, newSubtask: NewSubtask, userId: String): Subtask {
        log.info("Updating subtask with id: $id")

        val subtaskEntity = ctx.subtask.findOneById(id)
            ?: throw ResourceNotFoundException("Subtask with id $id not found")

        require(subtaskEntity.task.id == newSubtask.taskId) {
            "Task ID cannot be changed during update"
        }

        val updatedEntity = subtaskEntity.updatedWith(newSubtask, userId, ctx)
        val savedEntity = ctx.subtask.save(updatedEntity)
        val subtask = savedEntity.toModel()

        // Invalidate caches
        cacheService.invalidateSubtask(id)
        cacheService.invalidateTaskSubtasks(newSubtask.taskId)

        return subtask
    }

    @Transactional
    fun deleteSubtask(id: UUID) {
        log.info("Deleting subtask with id: $id")

        val subtask = ctx.subtask.findOneById(id)
            ?: throw ResourceNotFoundException("Subtask with id $id not found")

        val taskId = subtask.task.id

        // Invalidate caches
        cacheService.invalidateSubtask(id)
        if (taskId != null) {
            cacheService.invalidateTaskSubtasks(taskId)
        }

        ctx.subtask.delete(subtask)
    }

    @Transactional
    fun toggleSubtaskStatus(id: UUID, userId: String): Subtask {
        log.info("Toggling subtask status for id: $id")
        val subtask = ctx.subtask.findOneById(id)?.toModel()
            ?: throw ResourceNotFoundException("Subtask with id $id not found")

        val updatedSubtask = subtask.copy(isDone = !subtask.isDone)

        val newSubtask = NewSubtask(
            taskId = subtask.taskId,
            title = subtask.title,
            description = subtask.description,
            isDone = updatedSubtask.isDone,
            createdBy = subtask.createdBy,
            createdAt = subtask.createdAt
        )

        return updateSubtask(id, newSubtask, userId)
    }

    private fun getFromDbAndCache(id: UUID): Subtask {
        log.info("Fetching subtask $id from database")
        val subtask = ctx.subtask.findOneById(id)?.toModel()
            ?: throw ResourceNotFoundException("Subtask with id $id not found")

        cacheService.cacheSubtask(subtask)
        return subtask
    }
}