package dev.ecckea.agilepath.backend.domain.story.service

import dev.ecckea.agilepath.backend.domain.story.model.NewTask
import dev.ecckea.agilepath.backend.domain.story.model.Task
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
class TaskService(
    private val ctx: RepositoryContext
) : Logged() {

    @Transactional
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
    fun getTask(id: UUID): Task {
        return ctx.task.findOneById(id)?.toModel()
            ?: throw ResourceNotFoundException("Task with id $id not found")
    }

    @Transactional
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
    fun deleteTask(id: UUID) {
        val task = ctx.task.findOneById(id)
            ?: throw ResourceNotFoundException("Task with id $id not found")
        ctx.task.delete(task)
    }

    @Transactional(readOnly = true)
    fun getTasksByStoryId(storyId: UUID): List<Task> {
        return ctx.task.findByStoryId(storyId).map { it.toModel() }
    }
}