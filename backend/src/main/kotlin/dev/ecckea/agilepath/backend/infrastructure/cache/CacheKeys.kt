package dev.ecckea.agilepath.backend.infrastructure.cache

/**
 * Centralized cache key definitions for consistent naming across the application.
 *
 * This object provides constants and functions to generate versioned cache keys
 * for application entities and their relationships. The versioning (currently v1)
 * enables easier cache invalidation when data structures change.
 *
 * The key functions follow these patterns:
 * - Single entity keys: `{entityType}Key(id)` - for individual entities
 * - Relationship keys: `{parentEntity}{childEntities}Key(parentId)` - for collections
 *
 * All keys automatically include the version prefix.
 */
object CacheKeys {
    // Cache version prefix to ensure compatibility with future changes
    private const val VERSION = "v1:"

    // Entity-specific cache key prefixes
    private const val USER = "${VERSION}user:"
    private const val PROJECT = "${VERSION}project:"
    private const val USER_PROJECTS = "${VERSION}user:projects:"
    private const val SPRINT = "${VERSION}sprint:"
    private const val PROJECT_SPRINTS = "${VERSION}project:sprints:"
    private const val SPRINT_COLUMN = "${VERSION}sprintColumn:"
    private const val SPRINT_COLUMNS = "${VERSION}sprint:columns:"
    private const val STORY = "${VERSION}story:"
    private const val PROJECT_STORIES = "${VERSION}project:stories:"
    private const val TASK = "${VERSION}task:"
    private const val STORY_TASKS = "${VERSION}story:tasks:"
    private const val SPRINT_COLUMN_TASKS = "${VERSION}sprintColumn:tasks:"
    private const val SUBTASK = "${VERSION}subtask:"
    private const val TASK_SUBTASKS = "${VERSION}task:subtasks:"
    private const val COMMENT = "${VERSION}comment:"
    private const val STORY_COMMENTS = "${VERSION}story:comments:"
    private const val TASK_COMMENTS = "${VERSION}task:comments:"

    // Single entity cache keys

    /** Cache key for a user with the given ID */
    fun userKey(id: String) = "$USER$id"

    /** Cache key for a project with the given ID */
    fun projectKey(id: String) = "$PROJECT$id"

    /** Cache key for a sprint with the given ID */
    fun sprintKey(id: String) = "$SPRINT$id"

    /** Cache key for a sprint column with the given ID */
    fun sprintColumnKey(id: String) = "$SPRINT_COLUMN$id"

    /** Cache key for a story with the given ID */
    fun storyKey(id: String) = "$STORY$id"

    /** Cache key for a task with the given ID */
    fun taskKey(id: String) = "$TASK$id"

    /** Cache key for a subtask with the given ID */
    fun subtaskKey(id: String) = "$SUBTASK$id"

    /** Cache key for a comment with the given ID */
    fun commentKey(id: String) = "$COMMENT$id"

    // Relationship cache keys

    /** Cache key for all projects belonging to a user */
    fun userProjectsKey(userId: String) = "$USER_PROJECTS$userId"

    /** Cache key for all sprints in a project */
    fun projectSprintsKey(projectId: String) = "$PROJECT_SPRINTS$projectId"

    /** Cache key for all columns in a sprint */
    fun sprintColumnsKey(sprintId: String) = "$SPRINT_COLUMNS$sprintId"

    /** Cache key for all stories in a project */
    fun projectStoriesKey(projectId: String) = "$PROJECT_STORIES$projectId"

    /** Cache key for all tasks in a story */
    fun storyTasksKey(storyId: String) = "$STORY_TASKS$storyId"

    /** Cache key for all tasks in a sprint column */
    fun sprintColumnTasksKey(columnId: String) = "$SPRINT_COLUMN_TASKS$columnId"

    /** Cache key for all subtasks belonging to a task */
    fun taskSubtasksKey(taskId: String) = "$TASK_SUBTASKS$taskId"

    /** Cache key for all comments on a story */
    fun storyCommentsKey(storyId: String) = "$STORY_COMMENTS$storyId"

    /** Cache key for all comments on a task */
    fun taskCommentsKey(taskId: String) = "$TASK_COMMENTS$taskId"
}