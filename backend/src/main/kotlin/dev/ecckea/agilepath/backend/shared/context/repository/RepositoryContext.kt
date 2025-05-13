package dev.ecckea.agilepath.backend.shared.context.repository

import dev.ecckea.agilepath.backend.domain.column.repository.SprintColumnRepository
import dev.ecckea.agilepath.backend.domain.project.repository.ProjectRepository
import dev.ecckea.agilepath.backend.domain.sprint.repository.SprintRepository
import dev.ecckea.agilepath.backend.domain.story.repository.*
import dev.ecckea.agilepath.backend.domain.user.repository.UserRepository
import org.springframework.stereotype.Component

/**
 * Centralized container for all repositories in the application.
 *
 * This class provides a single point of access to all repositories, making dependency
 * injection simpler in service classes that need multiple repositories. It pairs well
 * with the repository extension functions defined in this package.
 *
 * Using RepositoryContext with the 'ref' extension functions creates a clean, fluent API:
 *
 * ```kotlin
 * // Instead of injecting multiple repositories:
 * @Autowired private lateinit var userRepository: UserRepository
 * @Autowired private lateinit var projectRepository: ProjectRepository
 *
 * // You can inject just the context:
 * @Autowired private lateinit var repositories: RepositoryContext
 *
 * // And use it with extension functions:
 * val userRef = repositories.user.ref(userId)
 * val projectRef = repositories.project.ref(projectId)
 * ```
 *
 * This approach reduces constructor parameter bloat, simplifies testing,
 * and makes code more readable by standardizing repository access patterns.
 */
@Component
class RepositoryContext(
    /** Repository for user-related database operations */
    val user: UserRepository,

    /** Repository for project-related database operations */
    val project: ProjectRepository,

    /** Repository for sprint-related database operations */
    val sprint: SprintRepository,

    /** Repository for sprint column-related database operations */
    val sprintColumn: SprintColumnRepository,

    /** Repository for story-related database operations */
    val story: StoryRepository,

    /** Repository for task-related database operations */
    val task: TaskRepository,

    /** Repository for task assignee relationship operations */
    val taskAssinee: TaskAssigneeRepository,

    /** Repository for subtask-related database operations */
    val subtask: SubtaskRepository,

    /** Repository for comment-related database operations */
    val comment: CommentRepository,
)