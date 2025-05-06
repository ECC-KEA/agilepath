package dev.ecckea.agilepath.backend.domain.story.service

import dev.ecckea.agilepath.backend.domain.story.model.Comment
import dev.ecckea.agilepath.backend.domain.story.model.NewComment
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toEntity
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toModel
import dev.ecckea.agilepath.backend.domain.story.model.mapper.updatedWith
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.exceptions.BadRequestException
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.exceptions.ValidationException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CommentService(
    private val ctx: RepositoryContext,
    private val cacheManager: CacheManager
) : Logged() {

    @Transactional(readOnly = true)
    @Cacheable(value = ["comments"], key = "#id")
    fun getComment(id: UUID): Comment {
        return ctx.comment.findOneById(id)?.toModel()
            ?: throw ResourceNotFoundException("Comment with id $id not found")
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["commentsByTask"], key = "#taskId")
    fun getCommentsByTaskId(taskId: UUID): List<Comment> {
        return ctx.comment.findByTaskId(taskId).map { it.toModel() }
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["commentsByStory"], key = "#storyId")
    fun getCommentsByStoryId(storyId: UUID): List<Comment> {
        return ctx.comment.findByStoryId(storyId).map { it.toModel() }
    }

    @Transactional
    fun createComment(newComment: NewComment): Comment {
        log.info("Creating new comment")

        validateCommentTarget(newComment.storyId, newComment.taskId)
        validateTargetExists(newComment.storyId, newComment.taskId)

        val commentEntity = newComment.toEntity(ctx)
        val saved = ctx.comment.save(commentEntity)

        // Manual eviction: storyId or taskId list
        newComment.taskId?.let {
            cacheManager.getCache("commentsByTask")?.evict(it)
        }
        newComment.storyId?.let {
            cacheManager.getCache("commentsByStory")?.evict(it)
        }

        return saved.toModel()
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

        // Evict individual and parent
        cacheManager.getCache("comments")?.evict(id)
        existingModel.taskId?.let { cacheManager.getCache("commentsByTask")?.evict(it) }
        existingModel.storyId?.let { cacheManager.getCache("commentsByStory")?.evict(it) }

        return saved.toModel()
    }

    @Transactional
    fun deleteComment(id: UUID) {
        log.info("Deleting comment with id: $id")

        val comment = ctx.comment.findOneById(id)
            ?: throw ResourceNotFoundException("Comment with id $id not found")

        // Manual eviction before delete
        cacheManager.getCache("comments")?.evict(id)
        comment.task?.id?.let { cacheManager.getCache("commentsByTask")?.evict(it) }
        comment.story?.id?.let { cacheManager.getCache("commentsByStory")?.evict(it) }

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
}