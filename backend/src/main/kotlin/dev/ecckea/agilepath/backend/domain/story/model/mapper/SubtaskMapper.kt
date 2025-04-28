package dev.ecckea.agilepath.backend.domain.story.model.mapper

import dev.ecckea.agilepath.backend.domain.story.dto.SubtaskRequest
import dev.ecckea.agilepath.backend.domain.story.dto.SubtaskResponse
import dev.ecckea.agilepath.backend.domain.story.model.NewSubtask
import dev.ecckea.agilepath.backend.domain.story.model.Subtask
import dev.ecckea.agilepath.backend.domain.story.repository.entity.SubtaskEntity
import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskEntity
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.context.repository.ref
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.security.currentUser
import dev.ecckea.agilepath.backend.shared.utils.now
import dev.ecckea.agilepath.backend.shared.utils.toZonedDateTime

fun SubtaskEntity.toModel(): Subtask {
    val entityId = id ?: throw ResourceNotFoundException("Subtask entity id is missing")
    val taskId = task.id ?: throw ResourceNotFoundException("Task ID is missing in SubtaskEntity")
    return Subtask(
        id = entityId,
        taskId = taskId,
        title = title,
        description = description,
        isDone = isDone,
        createdBy = createdBy.id,
        modifiedBy = modifiedBy?.id,
        createdAt = createdAt,
        modifiedAt = modifiedAt
    )
}

fun Subtask.toDTO() = SubtaskResponse(
    id = id,
    taskId = taskId,
    title = title,
    description = description,
    isDone = isDone,
    createdBy = createdBy,
    modifiedBy = modifiedBy,
    createdAt = toZonedDateTime(createdAt),
    modifiedAt = modifiedAt?.let { toZonedDateTime(it) }
)

fun Subtask.toEntity(
    task: TaskEntity,
    createdBy: UserEntity,
    modifiedBy: UserEntity? = null
): SubtaskEntity {
    return SubtaskEntity(
        id = id,
        task = task,
        title = title,
        description = description,
        isDone = isDone,
        createdBy = createdBy,
        modifiedBy = modifiedBy,
        createdAt = createdAt,
        modifiedAt = modifiedAt
    )
}

fun SubtaskRequest.toModel(userId: String = currentUser().id) = NewSubtask(
    taskId = taskId,
    title = title,
    description = description,
    isDone = isDone,
    createdBy = userId,
    createdAt = now()
)

fun NewSubtask.toEntity(ctx: RepositoryContext): SubtaskEntity {
    return SubtaskEntity(
        task = ctx.task.ref(taskId),
        title = title,
        description = description,
        isDone = isDone,
        createdBy = ctx.user.ref(createdBy),
        createdAt = createdAt,
    )
}

fun SubtaskEntity.updatedWith(update: NewSubtask, userId: String, ctx: RepositoryContext): SubtaskEntity {
    return SubtaskEntity(
        id = id,
        task = task,
        title = update.title,
        description = update.description,
        isDone = update.isDone,
        createdBy = createdBy,
        modifiedBy = ctx.user.ref(userId),
        createdAt = createdAt,
        modifiedAt = now()
    )
}