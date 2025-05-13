package dev.ecckea.agilepath.backend.domain.story.service

import dev.ecckea.agilepath.backend.domain.story.model.Comment
import dev.ecckea.agilepath.backend.domain.story.model.NewComment
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toEntity
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toModel
import dev.ecckea.agilepath.backend.domain.story.model.mapper.updatedWith
import dev.ecckea.agilepath.backend.infrastructure.cache.*
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.exceptions.BadRequestException
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.exceptions.ValidationException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CommentService(
    private val ctx: RepositoryContext,
    private val cacheService: CacheService
) : Logged() {

    @Transactional(readOnly = true)
    fun getComment(id: UUID): Comment {
        log.info("Fetching comment with id: $id")

        // Check if the comment is in the cache
        cacheService.getComment(id)?.let { return it }

        // If not in cache, get from database and cache it
        return getFromDbAndCache(id)
    }

    @Transactional(readOnly = true)
    fun getCommentsByTaskId(taskId: UUID): List<Comment> {
        log.info("Fetching comments for task with id: $taskId")

        // Check if the comments are in the cache
        cacheService.getTaskComments(taskId)?.let { return it }

        // If not in cache, get from database and cache it
        val comments = ctx.comment.findByTaskId(taskId).map { it.toModel() }

        cacheService.cacheTaskComments(taskId, comments)
        return comments
    }

    @Transactional(readOnly = true)
    fun getCommentsByStoryId(storyId: UUID): List<Comment> {
        log.info("Fetching comments for story with id: $storyId")

        // Check if the comments are in the cache
        cacheService.getStoryComments(storyId)?.let { return it }

        // If not in cache, get from database and cache it
        val comments = ctx.comment.findByStoryId(storyId).map { it.toModel() }

        cacheService.cacheStoryComments(storyId, comments)
        return comments
    }

    @Transactional
    fun createComment(newComment: NewComment): Comment {
        log.info("Creating new comment")

        validateCommentTarget(newComment.storyId, newComment.taskId)
        validateTargetExists(newComment.storyId, newComment.taskId)

        val commentEntity = newComment.toEntity(ctx)
        val saved = ctx.comment.save(commentEntity)
        val comment = saved.toModel()

        // Invalidate relevant caches
        newComment.taskId?.let {
            cacheService.invalidateTaskComments(it)
        }
        newComment.storyId?.let {
            cacheService.invalidateStoryComments(it)
        }

        return comment
    }

    @Transactional
    fun updateComment(id: UUID, newComment: NewComment, userId: String): Comment {
        log.info("Updating comment with id: $id")

        val commentEntity = ctx.comment.findOneById(id)
            ?: throw ResourceNotFoundException("Comment with id $id not found")

        val existingModel = commentEntity.toModel()
        validateTargetConsistency(existingModel, newComment)

        val updatedEntity = commentEntity.updatedWith(newComment, userId, ctx)
        val saved = ctx.comment.save(updatedEntity)
        val updatedComment = saved.toModel()

        // Invalidate caches
        cacheService.invalidateComment(id)
        existingModel.taskId?.let { cacheService.invalidateTaskComments(it) }
        existingModel.storyId?.let { cacheService.invalidateStoryComments(it) }

        return updatedComment
    }

    @Transactional
    fun deleteComment(id: UUID) {
        log.info("Deleting comment with id: $id")

        val comment = ctx.comment.findOneById(id)
            ?: throw ResourceNotFoundException("Comment with id $id not found")

        // Invalidate caches before delete
        cacheService.invalidateComment(id)
        comment.task?.id?.let { cacheService.invalidateTaskComments(it) }
        comment.story?.id?.let { cacheService.invalidateStoryComments(it) }

        ctx.comment.delete(comment)
    }


    private fun validateCommentTarget(storyId: UUID?, taskId: UUID?) {
        when {
            storyId != null && taskId != null ->
                throw ValidationException("Comment must be associated with either a story or a task, not both")

            storyId == null && taskId == null ->
                throw ValidationException("Comment must be associated with either a story or a task")
        }
    }


    private fun validateTargetExists(storyId: UUID?, taskId: UUID?) {
        if (storyId != null) {
            val storyExists = ctx.story.existsById(storyId)
            require(storyExists) { throw ResourceNotFoundException("Story with id $storyId not found") }
        } else if (taskId != null) {
            val taskExists = ctx.task.existsById(taskId)
            require(taskExists) { throw ResourceNotFoundException("Task with id $taskId not found") }
        }
    }


    private fun validateTargetConsistency(existingComment: Comment, newComment: NewComment) {
        // Ensure the parent entity remains the same
        val targetChanged = when {
            existingComment.storyId != null && newComment.storyId != existingComment.storyId -> true
            existingComment.taskId != null && newComment.taskId != existingComment.taskId -> true
            else -> false
        }

        require(!targetChanged) {
            throw BadRequestException("Cannot change the comment's target (story or task) during update")
        }
    }

    private fun getFromDbAndCache(id: UUID): Comment {
        log.info("Fetching comment $id from database")
        val comment = ctx.comment.findOneById(id)?.toModel()
            ?: throw ResourceNotFoundException("Comment with id $id not found")

        cacheService.cacheComment(comment)
        return comment
    }
}