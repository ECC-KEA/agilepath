package dev.ecckea.agilepath.backend.domain.story.model.mapper

import dev.ecckea.agilepath.backend.domain.story.dto.CommentResponse
import dev.ecckea.agilepath.backend.domain.story.dto.SubtaskResponse
import dev.ecckea.agilepath.backend.domain.story.dto.TaskRequest
import dev.ecckea.agilepath.backend.domain.story.dto.TaskResponse
import dev.ecckea.agilepath.backend.domain.story.model.NewTask
import dev.ecckea.agilepath.backend.domain.story.model.Comment
import dev.ecckea.agilepath.backend.domain.story.model.PointEstimate
import dev.ecckea.agilepath.backend.domain.story.model.Task
import dev.ecckea.agilepath.backend.domain.story.model.Subtask
import dev.ecckea.agilepath.backend.domain.story.model.TshirtEstimate
import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskEntity
import dev.ecckea.agilepath.backend.domain.user.model.mapper.toDTO
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.context.repository.ref
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.security.currentUser
import dev.ecckea.agilepath.backend.shared.utils.now
import dev.ecckea.agilepath.backend.shared.utils.toZonedDateTime

fun TaskEntity.toModel(comments: List<Comment>, subtasks: List<Subtask>): Task {
    val entityId = id ?: throw ResourceNotFoundException("Task entity id is missing")
    val storyId = story.id ?: throw ResourceNotFoundException("Story ID is missing in TaskEntity")
    val sprintColumnId = sprintColumn.id ?: throw ResourceNotFoundException("Sprint column ID is missing in TaskEntity")

    return Task(
        id = entityId,
        storyId = storyId,
        sprintColumnId = sprintColumnId,
        title = title,
        description = description,
        estimateTshirt = estimateTshirt,
        estimatePoints = estimatePoints,
        createdBy = createdBy.id,
        modifiedBy = modifiedBy?.id,
        createdAt = createdAt,
        modifiedAt = modifiedAt,
        comments = comments,
        subtasks = subtasks,
    )
}

fun Task.toDTO() = TaskResponse(
    id = id,
    storyId = storyId,
    sprintColumnId = sprintColumnId,
    title = title,
    description = description,
    estimateTshirt = estimateTshirt,
    estimatePoints = estimatePoints,
    comments = comments?.map { it.toDTO() },
    subtasks = subtasks?.map { it.toDTO() },
    assignees = assignees?.map { it.toDTO() },
    createdBy = createdBy,
    modifiedBy = modifiedBy,
    createdAt = toZonedDateTime(createdAt),
    modifiedAt = modifiedAt?.let { toZonedDateTime(it) },
)

fun Task.toDTO(subtasks: List<SubtaskResponse>) = TaskResponse(
    id = id,
    storyId = storyId,
    sprintColumnId = sprintColumnId,
    title = title,
    description = description,
    estimateTshirt = estimateTshirt,
    estimatePoints = estimatePoints,
    subtasks = subtasks,
    createdBy = createdBy,
    modifiedBy = modifiedBy,
    createdAt = toZonedDateTime(createdAt),
    modifiedAt = modifiedAt?.let { toZonedDateTime(it) },
)

fun Task.toDTO(comments: List<CommentResponse>, subtasks: List<SubtaskResponse>) = TaskResponse(
    id = id,
    storyId = storyId,
    sprintColumnId = sprintColumnId,
    title = title,
    description = description,
    estimateTshirt = estimateTshirt,
    estimatePoints = estimatePoints,
    comments = comments,
    subtasks = subtasks,
    createdBy = createdBy,
    modifiedBy = modifiedBy,
    createdAt = toZonedDateTime(createdAt),
    modifiedAt = modifiedAt?.let { toZonedDateTime(it) },
)

fun TaskRequest.toModel(userId: String = currentUser().id): NewTask {
    return NewTask(
        storyId = storyId,
        sprintColumnId = sprintColumnId,
        title = title,
        description = description,
        estimateTshirt = estimateTshirt?.let { TshirtEstimate.fromString(it) },
        estimatePoints = estimatePoints?.let { PointEstimate.fromString(it) },
        assigneeIds = assigneeIds,
        createdBy = userId,
        createdAt = now()
    )
}

fun NewTask.toEntity(ctx: RepositoryContext): TaskEntity {
    return TaskEntity(
        story = ctx.story.ref(storyId),
        sprintColumn = ctx.sprintColumn.ref(sprintColumnId),
        title = title,
        description = description,
        estimateTshirt = estimateTshirt,
        estimatePoints = estimatePoints,
        createdBy = ctx.user.ref(createdBy),
        modifiedBy = null,
        createdAt = createdAt,
        modifiedAt = null
    )
}

fun TaskEntity.updatedWith(update: NewTask, userId: String, ctx: RepositoryContext): TaskEntity {
    return TaskEntity(
        id = this.id,
        story = this.story,
        sprintColumn = ctx.sprintColumn.ref(update.sprintColumnId),
        title = update.title,
        description = update.description,
        estimateTshirt = update.estimateTshirt,
        estimatePoints = update.estimatePoints,
        createdBy = this.createdBy,
        modifiedBy = ctx.user.ref(userId),
        createdAt = this.createdAt,
        modifiedAt = now()
    )
}