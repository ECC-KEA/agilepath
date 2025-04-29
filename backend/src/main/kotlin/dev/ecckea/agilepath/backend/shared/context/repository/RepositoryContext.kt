package dev.ecckea.agilepath.backend.shared.context.repository

import dev.ecckea.agilepath.backend.domain.column.repository.SprintColumnRepository
import dev.ecckea.agilepath.backend.domain.project.repository.ProjectRepository
import dev.ecckea.agilepath.backend.domain.sprint.repository.SprintRepository
import dev.ecckea.agilepath.backend.domain.story.repository.*
import dev.ecckea.agilepath.backend.domain.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class RepositoryContext(
    val user: UserRepository,
    val project: ProjectRepository,
    val sprint: SprintRepository,
    val sprintColumn: SprintColumnRepository,
    val story: StoryRepository,
    val task: TaskRepository,
    val taskAssinee: TaskAssigneeRepository,
    val subtask: SubtaskRepository,
    val comment: CommentRepository,
    )