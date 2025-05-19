package dev.ecckea.agilepath.backend.infrastructure.cache

import com.fasterxml.jackson.core.type.TypeReference
import dev.ecckea.agilepath.backend.domain.story.model.Subtask
import java.util.*

/**
 * Subtask domain-specific extension functions for the CacheService.
 * These functions provide caching operations for individual subtasks and task-related subtasks.
 */

/**
 * Retrieves a subtask from the cache by its ID.
 *
 * @param id The unique identifier of the subtask.
 * @return The cached Subtask object, or null if not found.
 */
fun CacheService.getSubtask(id: UUID): Subtask? {
    val key = CacheKeys.subtaskKey(id.toString())
    return getFromCache(key, Subtask::class.java, "subtask $id")
}

/**
 * Caches a subtask with a specified time-to-live (TTL).
 *
 * @param subtask The Subtask object to cache.
 * @param ttlMinutes The time-to-live for the cached subtask in minutes. Defaults to 15 minutes.
 */
fun CacheService.cacheSubtask(subtask: Subtask, ttlMinutes: Long = 15) {
    val key = CacheKeys.subtaskKey(subtask.id.toString())
    setInCache(key, subtask, ttlMinutes, "subtask ${subtask.id}")
}

/**
 * Invalidates a cached subtask by its ID.
 *
 * @param id The unique identifier of the subtask to invalidate.
 */
fun CacheService.invalidateSubtask(id: UUID) {
    val key = CacheKeys.subtaskKey(id.toString())
    invalidateCache(key, "subtask $id")
}

/**
 * Task subtasks related extensions.
 */

/**
 * Retrieves a list of subtasks associated with a specific task from the cache.
 *
 * @param taskId The unique identifier of the task.
 * @return A list of cached Subtask objects, or null if not found.
 */
fun CacheService.getTaskSubtasks(taskId: UUID): List<Subtask>? {
    val key = CacheKeys.taskSubtasksKey(taskId.toString())
    return getListFromCache(key, object : TypeReference<List<Subtask>>() {}, "subtasks for task $taskId")
}

/**
 * Caches a list of subtasks for a specific task with a specified TTL.
 *
 * @param taskId The unique identifier of the task.
 * @param subtasks The list of Subtask objects to cache.
 * @param ttlMinutes The time-to-live for the cached subtasks in minutes. Defaults to 15 minutes.
 */
fun CacheService.cacheTaskSubtasks(taskId: UUID, subtasks: List<Subtask>, ttlMinutes: Long = 15) {
    val key = CacheKeys.taskSubtasksKey(taskId.toString())
    setInCache(key, subtasks, ttlMinutes, "subtasks for task $taskId")
}

/**
 * Invalidates cached subtasks for a specific task by its ID.
 *
 * @param taskId The unique identifier of the task.
 */
fun CacheService.invalidateTaskSubtasks(taskId: UUID) {
    val key = CacheKeys.taskSubtasksKey(taskId.toString())
    invalidateCache(key, "subtasks for task $taskId")
}