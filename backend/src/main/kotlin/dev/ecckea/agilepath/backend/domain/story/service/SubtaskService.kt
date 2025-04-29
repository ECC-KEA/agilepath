package dev.ecckea.agilepath.backend.domain.story.service

import dev.ecckea.agilepath.backend.domain.story.model.NewSubtask
import dev.ecckea.agilepath.backend.domain.story.model.Subtask
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toEntity
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toModel
import dev.ecckea.agilepath.backend.domain.story.model.mapper.updatedWith
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SubtaskService(
    private val ctx: RepositoryContext
) : Logged() {

    @Transactional
    fun createSubtask(newSubtask: NewSubtask): Subtask {
        log.info("Creating new subtask: {}", newSubtask.title)

        // Verify task exists
        val taskExists = ctx.task.existsById(newSubtask.taskId)
        require(taskExists) { throw ResourceNotFoundException("Task with id ${newSubtask.taskId} not found") }

        val subtaskEntity = newSubtask.toEntity(ctx)
        return ctx.subtask.save(subtaskEntity).toModel()
    }

    @Transactional(readOnly = true)
    fun getSubtask(id: UUID): Subtask {
        return ctx.subtask.findOneById(id)?.toModel()
            ?: throw ResourceNotFoundException("Subtask with id $id not found")
    }

    @Transactional
    fun updateSubtask(id: UUID, newSubtask: NewSubtask, userId: String): Subtask {
        val subtaskEntity = ctx.subtask.findOneById(id)
            ?: throw ResourceNotFoundException("Subtask with id $id not found")

        // Ensure task ID doesn't change during update
        require(subtaskEntity.task.id == newSubtask.taskId) {
            "Task ID cannot be changed during update"
        }

        val updatedEntity = subtaskEntity.updatedWith(newSubtask, userId, ctx)
        return ctx.subtask.save(updatedEntity).toModel()
    }

    @Transactional
    fun deleteSubtask(id: UUID) {
        val subtask = ctx.subtask.findOneById(id)
            ?: throw ResourceNotFoundException("Subtask with id $id not found")
        ctx.subtask.delete(subtask)
    }

    @Transactional(readOnly = true)
    fun getSubtasksByTaskId(taskId: UUID): List<Subtask> {
        return ctx.subtask.findByTaskId(taskId).map { it.toModel() }
    }

    @Transactional
    fun toggleSubtaskStatus(id: UUID, userId: String): Subtask {
        val subtask = ctx.subtask.findOneById(id)?.toModel()
            ?: throw ResourceNotFoundException("Subtask with id $id not found")

        val updatedSubtask = subtask.copy(isDone = !subtask.isDone)

        // Create a NewSubtask with the updated status
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
}