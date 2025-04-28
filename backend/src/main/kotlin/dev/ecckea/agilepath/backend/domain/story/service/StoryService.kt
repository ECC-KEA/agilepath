package dev.ecckea.agilepath.backend.domain.story.service

import dev.ecckea.agilepath.backend.domain.story.model.NewStory
import dev.ecckea.agilepath.backend.domain.story.model.Story
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
class StoryService(
    private val ctx: RepositoryContext
) : Logged() {

    @Transactional
    fun createStory(newStory: NewStory): Story {
        log.info("Creating story with title ${newStory.title}")

        if (!ctx.project.existsById(newStory.projectId)) {
            throw ResourceNotFoundException("Project with ID ${newStory.projectId} not found")
        }

        val entity = newStory.toEntity(ctx)
        val saved = ctx.story.save(entity)
        return saved.toModel()
    }

    @Transactional(readOnly = true)
    fun getStory(id: UUID): Story {
        log.info("Fetching story with id: $id")
        return ctx.story.findOneById(id)?.toModel()
            ?: throw ResourceNotFoundException("Story with id $id not found")
    }

    @Transactional
    fun updateStory(id: UUID, updated: NewStory, userId: String): Story {
        log.info("Updating story with id: $id")
        val existingEntity = ctx.story.findOneById(id)
            ?: throw ResourceNotFoundException("Story with id $id not found")
        val updatedEntity = existingEntity.updatedWith(updated, userId, ctx)
        val savedEntity = ctx.story.save(updatedEntity)
        return savedEntity.toModel()
    }

    @Transactional
    fun deleteStory(id: UUID) {
        log.info("Deleting story with id: $id")
        val entity = ctx.story.findOneById(id)
            ?: throw ResourceNotFoundException("Story with id $id not found")
        ctx.story.delete(entity)
    }
}