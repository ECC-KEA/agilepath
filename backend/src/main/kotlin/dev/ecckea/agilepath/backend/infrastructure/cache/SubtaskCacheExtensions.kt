package dev.ecckea.agilepath.backend.infrastructure.cache

import com.fasterxml.jackson.core.type.TypeReference
import dev.ecckea.agilepath.backend.domain.story.model.Subtask
import java.util.*

/**
 * Subtask domain-specific extension functions for the CacheService.
 */

fun CacheService.getSubtask(id: UUID): Subtask? {
    val key = CacheKeys.subtaskKey(id.toString())
    return getFromCache(key, Subtask::class.java, "subtask $id")
}

fun CacheService.cacheSubtask(subtask: Subtask, ttlMinutes: Long = 15) {
    val key = CacheKeys.subtaskKey(subtask.id.toString())
    setInCache(key, subtask, ttlMinutes, "subtask ${subtask.id}")
}

fun CacheService.invalidateSubtask(id: UUID) {
    val key = CacheKeys.subtaskKey(id.toString())
    invalidateCache(key, "subtask $id")
}

/**
 * Task subtasks related extensions
 */
fun CacheService.getTaskSubtasks(taskId: UUID): List<Subtask>? {
    val key = CacheKeys.taskSubtasksKey(taskId.toString())
    return getListFromCache(key, object : TypeReference<List<Subtask>>() {}, "subtasks for task $taskId")
}

fun CacheService.cacheTaskSubtasks(taskId: UUID, subtasks: List<Subtask>, ttlMinutes: Long = 15) {
    val key = CacheKeys.taskSubtasksKey(taskId.toString())
    setInCache(key, subtasks, ttlMinutes, "subtasks for task $taskId")
}

fun CacheService.invalidateTaskSubtasks(taskId: UUID) {
    val key = CacheKeys.taskSubtasksKey(taskId.toString())
    invalidateCache(key, "subtasks for task $taskId")
}