package dev.ecckea.agilepath.backend.domain.story.model.mapper

import dev.ecckea.agilepath.backend.domain.story.dto.CommentRequest
import dev.ecckea.agilepath.backend.domain.story.dto.CommentResponse
import dev.ecckea.agilepath.backend.domain.story.model.Comment
import dev.ecckea.agilepath.backend.domain.story.model.NewComment
import dev.ecckea.agilepath.backend.domain.story.repository.entity.CommentEntity
import dev.ecckea.agilepath.backend.domain.story.repository.entity.StoryEntity
import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskEntity
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.security.currentUser
import dev.ecckea.agilepath.backend.shared.utils.now
import dev.ecckea.agilepath.backend.shared.utils.toZonedDateTime

fun CommentEntity.toModel(): Comment {
    val entityId = id ?: throw ResourceNotFoundException("Comment entity id is missing")
    val storyId = story?.id
    val taskId = task?.id
    return Comment(
        id = entityId,
        content = content,
        storyId = storyId,
        taskId = taskId,
        createdBy = createdBy.id,
        modifiedBy = modifiedBy?.id,
        createdAt = createdAt,
        modifiedAt = modifiedAt
    )
}

fun Comment.toDTO() = CommentResponse(
    id = id,
    content = content,
    storyId = storyId,
    taskId = taskId,
    createdBy = createdBy,
    modifiedBy = modifiedBy,
    createdAt = toZonedDateTime(createdAt),
    modifiedAt = modifiedAt?.let { toZonedDateTime(it) },
)

fun Comment.toEntity(
    story: StoryEntity?,
    task: TaskEntity?,
    createdBy: UserEntity,
    modifiedBy: UserEntity? = null
): CommentEntity {
    return CommentEntity(
        id = id,
        story = story,
        task = task,
        content = content,
        createdBy = createdBy,
        modifiedBy = modifiedBy,
        createdAt = createdAt,
        modifiedAt = modifiedAt
    )
}

fun CommentRequest.toModel(userId: String = currentUser().id) = NewComment(
    content = content,
    storyId = storyId,
    taskId = taskId,
    createdBy = userId,
    createdAt = now()
)

fun NewComment.toEntity(
    story: StoryEntity?,
    task: TaskEntity?,
    createdBy: UserEntity,
): CommentEntity {
    return CommentEntity(
        story = story,
        task = task,
        content = content,
        createdBy = createdBy,
        createdAt = createdAt,
    )
}

fun CommentEntity.updatedWith(update: NewComment, modifiedBy: UserEntity): CommentEntity {
    return CommentEntity(
        id = id,
        story = story,
        task = task,
        content = update.content,
        createdBy = createdBy,
        modifiedBy = modifiedBy,
        createdAt = createdAt,
        modifiedAt = now()
    )
}