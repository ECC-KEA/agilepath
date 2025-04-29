package dev.ecckea.agilepath.backend.domain.story.service

import dev.ecckea.agilepath.backend.domain.story.model.Comment
import dev.ecckea.agilepath.backend.domain.story.model.NewComment
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toEntity
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toModel
import dev.ecckea.agilepath.backend.domain.story.model.mapper.updatedWith
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.exceptions.BadRequestException
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CommentService(
    private val ctx: RepositoryContext
) : Logged() {

    @Transactional(readOnly = true)
    fun getComment(id: UUID): Comment {
        return ctx.comment.findOneById(id)?.toModel()
            ?: throw ResourceNotFoundException("Comment with id $id not found")
    }

    @Transactional(readOnly = true)
    fun getCommentsByTaskId(taskId: UUID): List<Comment> {
        return ctx.comment.findByTaskId(taskId).map { it.toModel() }
    }

    @Transactional(readOnly = true)
    fun getCommentsByStoryId(storyId: UUID): List<Comment> {
        return ctx.comment.findByStoryId(storyId).map { it.toModel() }
    }

    @Transactional
    fun createComment(newComment: NewComment): Comment {
        log.info("Creating new comment")

        // Validate that exactly one of storyId or taskId is provided
        validateCommentTarget(newComment.storyId, newComment.taskId)

        // Verify target exists
        validateTargetExists(newComment.storyId, newComment.taskId)

        val commentEntity = newComment.toEntity(ctx)
        return ctx.comment.save(commentEntity).toModel()
    }

    @Transactional
    fun updateComment(id: UUID, newComment: NewComment, userId: String): Comment {
        log.info("Updating comment with id: $id")

        val commentEntity = ctx.comment.findOneById(id)
            ?: throw ResourceNotFoundException("Comment with id $id not found")

        // Ensure the target (story or task) doesn't change
        validateTargetConsistency(commentEntity.toModel(), newComment)

        val updatedEntity = commentEntity.updatedWith(newComment, userId, ctx)
        return ctx.comment.save(updatedEntity).toModel()
    }

    @Transactional
    fun deleteComment(id: UUID) {
        log.info("Deleting comment with id: $id")

        val comment = ctx.comment.findOneById(id)
            ?: throw ResourceNotFoundException("Comment with id $id not found")

        ctx.comment.delete(comment)
    }

    /**
     * Validates that exactly one of storyId or taskId is provided
     */
    private fun validateCommentTarget(storyId: UUID?, taskId: UUID?) {
        when {
            storyId != null && taskId != null ->
                throw BadRequestException("Comment must be associated with either a story or a task, not both")

            storyId == null && taskId == null ->
                throw BadRequestException("Comment must be associated with either a story or a task")
        }
    }

    /**
     * Validates that the target (story or task) exists
     */
    private fun validateTargetExists(storyId: UUID?, taskId: UUID?) {
        if (storyId != null) {
            val storyExists = ctx.story.existsById(storyId)
            require(storyExists) { throw ResourceNotFoundException("Story with id $storyId not found") }
        } else if (taskId != null) {
            val taskExists = ctx.task.existsById(taskId)
            require(taskExists) { throw ResourceNotFoundException("Task with id $taskId not found") }
        }
    }

    /**
     * Validates that the comment's target (story or task) hasn't changed during update
     */
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