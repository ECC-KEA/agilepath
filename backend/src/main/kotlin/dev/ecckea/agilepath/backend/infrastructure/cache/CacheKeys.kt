package dev.ecckea.agilepath.backend.infrastructure.cache

/**
 * Centralized cache key definitions for consistent cache naming across the application.
 */
object CacheKeys {
    private const val VERSION = "v1:"

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

    fun userKey(id: String) = "$USER$id"
    fun projectKey(id: String) = "$PROJECT$id"
    fun userProjectsKey(userId: String) = "$USER_PROJECTS$userId"
    fun sprintKey(id: String) = "$SPRINT$id"
    fun projectSprintsKey(projectId: String) = "$PROJECT_SPRINTS$projectId"
    fun sprintColumnKey(id: String) = "$SPRINT_COLUMN$id"
    fun sprintColumnsKey(sprintId: String) = "$SPRINT_COLUMNS$sprintId"
    fun storyKey(id: String) = "$STORY$id"
    fun projectStoriesKey(projectId: String) = "$PROJECT_STORIES$projectId"
    fun taskKey(id: String) = "$TASK$id"
    fun storyTasksKey(storyId: String) = "$STORY_TASKS$storyId"
    fun sprintColumnTasksKey(columnId: String) = "$SPRINT_COLUMN_TASKS$columnId"
    fun subtaskKey(id: String) = "$SUBTASK$id"
    fun taskSubtasksKey(taskId: String) = "$TASK_SUBTASKS$taskId"
    fun commentKey(id: String) = "$COMMENT$id"
    fun storyCommentsKey(storyId: String) = "$STORY_COMMENTS$storyId"
    fun taskCommentsKey(taskId: String) = "$TASK_COMMENTS$taskId"
}

