package dev.ecckea.agilepath.backend.domain.story.application

import dev.ecckea.agilepath.backend.domain.story.model.Comment
import dev.ecckea.agilepath.backend.domain.story.model.NewComment
import dev.ecckea.agilepath.backend.domain.story.model.StoryEventType
import dev.ecckea.agilepath.backend.domain.story.model.TaskEventType
import dev.ecckea.agilepath.backend.domain.story.service.CommentService
import dev.ecckea.agilepath.backend.domain.user.service.UserService
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.logging.events.StoryEventLogger
import dev.ecckea.agilepath.backend.shared.logging.events.TaskEventLogger
import dev.ecckea.agilepath.backend.shared.security.currentUser
import org.springframework.stereotype.Service
import java.util.*

@Service
class CommentApplication(
    private val commentService: CommentService,
    private val userService: UserService,
    private val taskEventLogger: TaskEventLogger,
    private val storyEventLogger: StoryEventLogger
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
        val createdComment = commentService.createComment(newComment)
        val user = userService.getById(newComment.createdBy)

        when {
            newComment.taskId != null -> {
                taskEventLogger.logEvent(
                    entityId = newComment.taskId,
                    eventType = TaskEventType.COMMENT_ADDED,
                    triggeredBy = user,
                    oldValue = null,
                    newValue = createdComment.content
                )
            }

            newComment.storyId != null -> {
                storyEventLogger.logEvent(
                    entityId = newComment.storyId,
                    eventType = StoryEventType.COMMENT_ADDED,
                    triggeredBy = user,
                    oldValue = null,
                    newValue = createdComment.content
                )
            }
        }

        return createdComment
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