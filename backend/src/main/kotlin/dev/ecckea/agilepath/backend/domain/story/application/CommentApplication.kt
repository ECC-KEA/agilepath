package dev.ecckea.agilepath.backend.domain.story.application

import dev.ecckea.agilepath.backend.domain.story.model.Comment
import dev.ecckea.agilepath.backend.domain.story.model.NewComment
import dev.ecckea.agilepath.backend.domain.story.service.CommentService
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.security.currentUser
import org.springframework.stereotype.Service
import java.util.*

@Service
class CommentApplication(
    private val commentService: CommentService
) : Logged() {

    fun getComment(id: UUID): Comment {
        log.info("Getting comment with id: {}", id)
        return commentService.getComment(id)
    }

    fun getCommentsByTaskId(taskId: UUID): List<Comment> {
        log.info("Getting comments for task: {}", taskId)
        return commentService.getCommentsByTaskId(taskId)
    }

    fun getCommentsByStoryId(storyId: UUID): List<Comment> {
        log.info("Getting comments for story: {}", storyId)
        return commentService.getCommentsByStoryId(storyId)
    }

    fun createComment(newComment: NewComment): Comment {
        log.info("Creating comment")
        return commentService.createComment(newComment)
    }

    fun updateComment(id: UUID, newComment: NewComment): Comment {
        log.info("Updating comment with id: {}", id)
        return commentService.updateComment(id, newComment, currentUser().id)
    }

    fun deleteComment(id: UUID) {
        log.info("Deleting comment with id: {}", id)
        commentService.deleteComment(id)
    }
}