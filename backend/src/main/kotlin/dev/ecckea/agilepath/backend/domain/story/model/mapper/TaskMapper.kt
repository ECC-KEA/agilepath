package dev.ecckea.agilepath.backend.domain.story.model.mapper

import dev.ecckea.agilepath.backend.domain.column.repository.entity.SprintColumnEntity
import dev.ecckea.agilepath.backend.domain.story.dto.TaskRequest
import dev.ecckea.agilepath.backend.domain.story.dto.TaskResponse
import dev.ecckea.agilepath.backend.domain.story.model.NewTask
import dev.ecckea.agilepath.backend.domain.story.model.Task
import dev.ecckea.agilepath.backend.domain.story.repository.entity.StoryEntity
import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskEntity
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.security.currentUser
import dev.ecckea.agilepath.backend.shared.utils.now
import dev.ecckea.agilepath.backend.shared.utils.toZonedDateTime

fun TaskEntity.toModel(): Task {
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
        modifiedAt = modifiedAt
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
    createdBy = createdBy,
    modifiedBy = modifiedBy,
    createdAt = toZonedDateTime(createdAt),
    modifiedAt = modifiedAt?.let { toZonedDateTime(it) },
)

fun Task.toEntity(
    story: StoryEntity,
    sprintColumn: SprintColumnEntity,
    createdBy: UserEntity,
    modifiedBy: UserEntity? = null
): TaskEntity {
    return TaskEntity(
        id = id,
        story = story,
        sprintColumn = sprintColumn,
        title = title,
        description = description,
        estimateTshirt = estimateTshirt,
        estimatePoints = estimatePoints,
        createdBy = createdBy,
        modifiedBy = modifiedBy,
        createdAt = createdAt,
        modifiedAt = modifiedAt
    )
}

fun TaskRequest.toModel(userId: String = currentUser().id): NewTask {
    return NewTask(
        storyId = storyId,
        sprintColumnId = sprintColumnId,
        title = title,
        description = description,
        estimateTshirt = estimateTshirt,
        estimatePoints = estimatePoints,
        createdBy = userId,
        createdAt = now()
    )
}

fun NewTask.toEntity(
    story: StoryEntity,
    sprintColumn: SprintColumnEntity,
    createdBy: UserEntity,
): TaskEntity {
    return TaskEntity(
        story = story,
        sprintColumn = sprintColumn,
        title = title,
        description = description,
        estimateTshirt = estimateTshirt,
        estimatePoints = estimatePoints,
        createdBy = createdBy,
        modifiedBy = null,
        createdAt = createdAt,
        modifiedAt = null
    )
}

fun TaskEntity.updatedWith(update: NewTask, modifiedBy: UserEntity): TaskEntity {
    return TaskEntity(
        id = this.id,
        story = this.story,
        sprintColumn = this.sprintColumn,
        title = update.title,
        description = update.description,
        estimateTshirt = update.estimateTshirt,
        estimatePoints = update.estimatePoints,
        createdBy = this.createdBy,
        modifiedBy = modifiedBy,
        createdAt = this.createdAt,
        modifiedAt = now()
    )
}