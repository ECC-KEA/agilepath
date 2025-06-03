package dev.ecckea.agilepath.backend.domain.story.service

import dev.ecckea.agilepath.backend.domain.story.model.NewStory
import dev.ecckea.agilepath.backend.domain.story.model.Story
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
class StoryService(
    private val ctx: RepositoryContext,
    private val cacheService: CacheService
) : Logged() {

    @Transactional
    fun createStory(newStory: NewStory): Story {
        log.info("Creating story with title ${newStory.title}")

        if (!ctx.project.existsById(newStory.projectId)) {
            throw ResourceNotFoundException("Project with ID ${newStory.projectId} not found")
        }

        val entity = newStory.toEntity(ctx)
        val saved = ctx.story.save(entity)

        // Invalidate project stories cache
        cacheService.invalidateProjectStories(newStory.projectId)

        return saved.toModel(emptyList(), emptyList())
    }

    @Transactional(readOnly = true)
    fun getStory(id: UUID): Story {
        log.info("Fetching story with id: $id")

        // Check if the story is in the cache
        cacheService.getStory(id)?.let { return it }

        // If not, fetch from the database and cache it
        return getFromDbAndCache(id)
    }

    @Transactional
    fun updateStory(id: UUID, updated: NewStory, userId: String): Story {
        log.info("Updating story with id: $id")
        val existingEntity = ctx.story.findOneById(id)
            ?: throw ResourceNotFoundException("Story with id $id not found")
        val updatedEntity = existingEntity.updatedWith(updated, userId, ctx)
        val savedEntity = ctx.story.save(updatedEntity)
        val tasks = ctx.task.findByStoryId(id).map{
            val taskId = it.id!!
            val cmts = ctx.comment.findByTaskId(taskId).map { it.toModel() }
            val subtasks = ctx.subtask.findByTaskId(taskId).map { it.toModel() }
            it.toModel(cmts, subtasks)
        }
        val comments = ctx.comment.findByStoryId(id).map{it.toModel()}

        // Invalidate caches
        cacheService.invalidateStory(id)
        cacheService.invalidateProjectStories(updated.projectId)

        return savedEntity.toModel(comments, tasks)
    }

    @Transactional
    fun deleteStory(id: UUID) {
        log.info("Deleting story with id: $id")
        val entity = ctx.story.findOneById(id)
            ?: throw ResourceNotFoundException("Story with id $id not found")

        val projectId = entity.project.id

        // Invalidate caches
        cacheService.invalidateStory(id)
        if (projectId != null) {
            cacheService.invalidateProjectStories(projectId)
        }

        ctx.story.delete(entity)
    }

    @Transactional(readOnly = true)
    fun getStoriesByProjectId(projectId: UUID): List<Story> {
        log.info("Fetching stories for project with id: $projectId")
        if (!ctx.project.existsById(projectId)) {
            throw ResourceNotFoundException("Project with ID $projectId not found")
        }
        return ctx.story.findAllByProjectId(projectId).map { 
            val storyId = it.id!!
            val tasks = ctx.task.findByStoryId(storyId).map{
                val taskId = it.id!!
                val cmts = ctx.comment.findByTaskId(taskId).map { it.toModel() }
                val subtasks = ctx.subtask.findByTaskId(taskId).map { it.toModel() }
                it.toModel(cmts, subtasks)
            }
            val comments = ctx.comment.findByStoryId(storyId).map{it.toModel()}
            it.toModel(comments, tasks)
        }
    }

    private fun getFromDbAndCache(id: UUID): Story {
        log.info("Fetching story $id from database")
        val tasks = ctx.task.findByStoryId(id).map{
            val taskId = it.id!!
            val cmts = ctx.comment.findByTaskId(taskId).map { it.toModel() }
            val subtasks = ctx.subtask.findByTaskId(taskId).map { it.toModel() }
            it.toModel(cmts, subtasks)
        }
        val comments = ctx.comment.findByStoryId(id).map{it.toModel()}
        val story = ctx.story.findOneById(id)?.toModel(comments, tasks)
            ?: throw ResourceNotFoundException("Story with id $id not found")

        cacheService.cacheStory(story)
        return story
    }
}