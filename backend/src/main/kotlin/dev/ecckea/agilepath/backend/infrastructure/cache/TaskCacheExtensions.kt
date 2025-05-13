package dev.ecckea.agilepath.backend.infrastructure.cache

import com.fasterxml.jackson.core.type.TypeReference
import dev.ecckea.agilepath.backend.domain.story.model.Task
import java.util.*

/**
 * Task domain-specific extension functions for the CacheService.
 */

fun CacheService.getTask(id: UUID): Task? {
    val key = CacheKeys.taskKey(id.toString())
    return getFromCache(key, Task::class.java, "task $id")
}

fun CacheService.cacheTask(task: Task, ttlMinutes: Long = 15) {
    val key = CacheKeys.taskKey(task.id.toString())
    setInCache(key, task, ttlMinutes, "task ${task.id}")
}

fun CacheService.invalidateTask(id: UUID) {
    val key = CacheKeys.taskKey(id.toString())
    invalidateCache(key, "task $id")
}

/**
 * Story tasks related extensions
 */
fun CacheService.getStoryTasks(storyId: UUID): List<Task>? {
    val key = CacheKeys.storyTasksKey(storyId.toString())
    return getListFromCache(key, object : TypeReference<List<Task>>() {}, "tasks for story $storyId")
}

fun CacheService.cacheStoryTasks(storyId: UUID, tasks: List<Task>, ttlMinutes: Long = 15) {
    val key = CacheKeys.storyTasksKey(storyId.toString())
    setInCache(key, tasks, ttlMinutes, "tasks for story $storyId")
}

fun CacheService.invalidateStoryTasks(storyId: UUID) {
    val key = CacheKeys.storyTasksKey(storyId.toString())
    invalidateCache(key, "tasks for story $storyId")
}

/**
 * Sprint column tasks related extensions
 */
fun CacheService.getSprintColumnTasks(columnId: UUID): List<Task>? {
    val key = CacheKeys.sprintColumnTasksKey(columnId.toString())
    return getListFromCache(key, object : TypeReference<List<Task>>() {}, "tasks for sprint column $columnId")
}

fun CacheService.cacheSprintColumnTasks(columnId: UUID, tasks: List<Task>, ttlMinutes: Long = 15) {
    val key = CacheKeys.sprintColumnTasksKey(columnId.toString())
    setInCache(key, tasks, ttlMinutes, "tasks for sprint column $columnId")
}

fun CacheService.invalidateSprintColumnTasks(columnId: UUID) {
    val key = CacheKeys.sprintColumnTasksKey(columnId.toString())
    invalidateCache(key, "tasks for sprint column $columnId")
}