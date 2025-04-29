package dev.ecckea.agilepath.backend.domain.story.model.mapper

import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEntity
import dev.ecckea.agilepath.backend.domain.story.dto.CommentResponse
import dev.ecckea.agilepath.backend.domain.story.dto.StoryRequest
import dev.ecckea.agilepath.backend.domain.story.dto.StoryResponse
import dev.ecckea.agilepath.backend.domain.story.dto.TaskResponse
import dev.ecckea.agilepath.backend.domain.story.model.NewStory
import dev.ecckea.agilepath.backend.domain.story.model.Story
import dev.ecckea.agilepath.backend.domain.story.repository.entity.StoryEntity
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.context.repository.ref
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.security.currentUser
import dev.ecckea.agilepath.backend.shared.utils.now
import dev.ecckea.agilepath.backend.shared.utils.toZonedDateTime

fun StoryEntity.toModel(): Story {
    val entityId = id ?: throw ResourceNotFoundException("Story entity id is missing")
    val projectId = project.id ?: throw ResourceNotFoundException("Project ID is missing in StoryEntity")

    return Story(
        id = entityId,
        projectId = projectId,
        title = title,
        description = description,
        status = status,
        priority = priority,
        createdBy = createdBy.id,
        modifiedBy = modifiedBy?.id,
        createdAt = createdAt,
        modifiedAt = modifiedAt,
    )
}

fun Story.toDTO() = StoryResponse(
    id = id,
    projectId = projectId,
    title = title,
    description = description,
    status = status,
    priority = priority,
    createdBy = createdBy,
    modifiedBy = modifiedBy,
    createdAt = toZonedDateTime(createdAt),
    modifiedAt = modifiedAt?.let { toZonedDateTime(it) },
)

fun Story.toDTO(comments: List<CommentResponse>, tasks: List<TaskResponse>) = StoryResponse(
    id = id,
    projectId = projectId,
    title = title,
    description = description,
    status = status,
    priority = priority,
    comments = comments,
    tasks = tasks,
    createdBy = createdBy,
    modifiedBy = modifiedBy,
    createdAt = toZonedDateTime(createdAt),
    modifiedAt = modifiedAt?.let { toZonedDateTime(it) },
)

fun Story.toEntity(
    project: ProjectEntity,
    createdBy: UserEntity,
    modifiedBy: UserEntity? = null
): StoryEntity {
    return StoryEntity(
        id = id,
        project = project,
        title = title,
        description = description,
        status = status,
        priority = priority,
        createdBy = createdBy,
        modifiedBy = modifiedBy,
        createdAt = createdAt,
        modifiedAt = modifiedAt
    )
}

fun StoryRequest.toModel(userId: String = currentUser().id) = NewStory(
    projectId = projectId,
    title = title,
    description = description,
    status = status,
    priority = priority,
    createdBy = userId,
    createdAt = now()
)

fun NewStory.toEntity(ctx: RepositoryContext): StoryEntity {
    return StoryEntity(
        project = ctx.project.ref(projectId),
        title = title,
        description = description,
        status = status,
        priority = priority,
        createdBy = ctx.user.ref(createdBy),
        createdAt = createdAt
    )
}

fun StoryEntity.updatedWith(update: NewStory, userId: String, ctx: RepositoryContext): StoryEntity {
    return StoryEntity(
        id = this.id,
        project = this.project,
        title = update.title,
        description = update.description,
        status = update.status,
        priority = update.priority,
        createdBy = this.createdBy,
        modifiedBy = ctx.user.ref(userId),
        createdAt = this.createdAt,
        modifiedAt = now()
    )
}