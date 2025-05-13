package dev.ecckea.agilepath.backend.infrastructure.cache

import com.fasterxml.jackson.core.type.TypeReference
import dev.ecckea.agilepath.backend.domain.story.model.Task
import java.util.*

/**
 * Task domain-specific extension functions for the CacheService.
 * These functions provide caching operations for individual tasks, story-related tasks,
 * and sprint column-related tasks.
 */

/**
 * Retrieves a task from the cache by its ID.
 *
 * @param id The unique identifier of the task.
 * @return The cached Task object, or null if not found.
 */
fun CacheService.getTask(id: UUID): Task? {
    val key = CacheKeys.taskKey(id.toString())
    return getFromCache(key, Task::class.java, "task $id")
}

/**
 * Caches a task with a specified time-to-live (TTL).
 *
 * @param task The Task object to cache.
 * @param ttlMinutes The time-to-live for the cached task in minutes. Defaults to 15 minutes.
 */
fun CacheService.cacheTask(task: Task, ttlMinutes: Long = 15) {
    val key = CacheKeys.taskKey(task.id.toString())
    setInCache(key, task, ttlMinutes, "task ${task.id}")
}

/**
 * Invalidates a cached task by its ID.
 *
 * @param id The unique identifier of the task to invalidate.
 */
fun CacheService.invalidateTask(id: UUID) {
    val key = CacheKeys.taskKey(id.toString())
    invalidateCache(key, "task $id")
}

/**
 * Story tasks related extensions.
 * These functions handle caching operations for tasks associated with a specific story.
 */

/**
 * Retrieves a list of tasks associated with a specific story from the cache.
 *
 * @param storyId The unique identifier of the story.
 * @return A list of cached Task objects, or null if not found.
 */
fun CacheService.getStoryTasks(storyId: UUID): List<Task>? {
    val key = CacheKeys.storyTasksKey(storyId.toString())
    return getListFromCache(key, object : TypeReference<List<Task>>() {}, "tasks for story $storyId")
}

/**
 * Caches a list of tasks for a specific story with a specified TTL.
 *
 * @param storyId The unique identifier of the story.
 * @param tasks The list of Task objects to cache.
 * @param ttlMinutes The time-to-live for the cached tasks in minutes. Defaults to 15 minutes.
 */
fun CacheService.cacheStoryTasks(storyId: UUID, tasks: List<Task>, ttlMinutes: Long = 15) {
    val key = CacheKeys.storyTasksKey(storyId.toString())
    setInCache(key, tasks, ttlMinutes, "tasks for story $storyId")
}

/**
 * Invalidates cached tasks for a specific story by its ID.
 *
 * @param storyId The unique identifier of the story.
 */
fun CacheService.invalidateStoryTasks(storyId: UUID) {
    val key = CacheKeys.storyTasksKey(storyId.toString())
    invalidateCache(key, "tasks for story $storyId")
}

/**
 * Sprint column tasks related extensions.
 * These functions handle caching operations for tasks associated with a specific sprint column.
 */

/**
 * Retrieves a list of tasks associated with a specific sprint column from the cache.
 *
 * @param columnId The unique identifier of the sprint column.
 * @return A list of cached Task objects, or null if not found.
 */
fun CacheService.getSprintColumnTasks(columnId: UUID): List<Task>? {
    val key = CacheKeys.sprintColumnTasksKey(columnId.toString())
    return getListFromCache(key, object : TypeReference<List<Task>>() {}, "tasks for sprint column $columnId")
}

/**
 * Caches a list of tasks for a specific sprint column with a specified TTL.
 *
 * @param columnId The unique identifier of the sprint column.
 * @param tasks The list of Task objects to cache.
 * @param ttlMinutes The time-to-live for the cached tasks in minutes. Defaults to 15 minutes.
 */
fun CacheService.cacheSprintColumnTasks(columnId: UUID, tasks: List<Task>, ttlMinutes: Long = 15) {
    val key = CacheKeys.sprintColumnTasksKey(columnId.toString())
    setInCache(key, tasks, ttlMinutes, "tasks for sprint column $columnId")
}

/**
 * Invalidates cached tasks for a specific sprint column by its ID.
 *
 * @param columnId The unique identifier of the sprint column.
 */
fun CacheService.invalidateSprintColumnTasks(columnId: UUID) {
    val key = CacheKeys.sprintColumnTasksKey(columnId.toString())
    invalidateCache(key, "tasks for sprint column $columnId")
}