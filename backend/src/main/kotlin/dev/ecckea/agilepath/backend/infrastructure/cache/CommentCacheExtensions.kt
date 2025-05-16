package dev.ecckea.agilepath.backend.infrastructure.cache

import com.fasterxml.jackson.core.type.TypeReference
import dev.ecckea.agilepath.backend.domain.story.model.Comment
import java.util.*

/**
 * Comment domain-specific extension functions for the CacheService.
 * These functions provide caching operations for individual comments, story comments, and task comments.
 */

/**
 * Retrieves a comment from the cache by its ID.
 *
 * @param id The unique identifier of the comment.
 * @return The cached Comment object, or null if not found.
 */
fun CacheService.getComment(id: UUID): Comment? {
    val key = CacheKeys.commentKey(id.toString())
    return getFromCache(key, Comment::class.java, "comment $id")
}

/**
 * Caches a comment with a specified time-to-live (TTL).
 *
 * @param comment The Comment object to cache.
 * @param ttlMinutes The time-to-live for the cached comment in minutes. Defaults to 15 minutes.
 */
fun CacheService.cacheComment(comment: Comment, ttlMinutes: Long = 15) {
    val key = CacheKeys.commentKey(comment.id.toString())
    setInCache(key, comment, ttlMinutes, "comment ${comment.id}")
}

/**
 * Invalidates a cached comment by its ID.
 *
 * @param id The unique identifier of the comment to invalidate.
 */
fun CacheService.invalidateComment(id: UUID) {
    val key = CacheKeys.commentKey(id.toString())
    invalidateCache(key, "comment $id")
}

/**
 * Retrieves a list of comments for a specific story from the cache.
 *
 * @param storyId The unique identifier of the story.
 * @return A list of cached Comment objects, or null if not found.
 */
fun CacheService.getStoryComments(storyId: UUID): List<Comment>? {
    val key = CacheKeys.storyCommentsKey(storyId.toString())
    return getListFromCache(key, object : TypeReference<List<Comment>>() {}, "comments for story $storyId")
}

/**
 * Caches a list of comments for a specific story with a specified TTL.
 *
 * @param storyId The unique identifier of the story.
 * @param comments The list of Comment objects to cache.
 * @param ttlMinutes The time-to-live for the cached comments in minutes. Defaults to 15 minutes.
 */
fun CacheService.cacheStoryComments(storyId: UUID, comments: List<Comment>, ttlMinutes: Long = 15) {
    val key = CacheKeys.storyCommentsKey(storyId.toString())
    setInCache(key, comments, ttlMinutes, "comments for story $storyId")
}

/**
 * Invalidates cached comments for a specific story by its ID.
 *
 * @param storyId The unique identifier of the story.
 */
fun CacheService.invalidateStoryComments(storyId: UUID) {
    val key = CacheKeys.storyCommentsKey(storyId.toString())
    invalidateCache(key, "comments for story $storyId")
}

/**
 * Retrieves a list of comments for a specific task from the cache.
 *
 * @param taskId The unique identifier of the task.
 * @return A list of cached Comment objects, or null if not found.
 */
fun CacheService.getTaskComments(taskId: UUID): List<Comment>? {
    val key = CacheKeys.taskCommentsKey(taskId.toString())
    return getListFromCache(key, object : TypeReference<List<Comment>>() {}, "comments for task $taskId")
}

/**
 * Caches a list of comments for a specific task with a specified TTL.
 *
 * @param taskId The unique identifier of the task.
 * @param comments The list of Comment objects to cache.
 * @param ttlMinutes The time-to-live for the cached comments in minutes. Defaults to 15 minutes.
 */
fun CacheService.cacheTaskComments(taskId: UUID, comments: List<Comment>, ttlMinutes: Long = 15) {
    val key = CacheKeys.taskCommentsKey(taskId.toString())
    setInCache(key, comments, ttlMinutes, "comments for task $taskId")
}

/**
 * Invalidates cached comments for a specific task by its ID.
 *
 * @param taskId The unique identifier of the task.
 */
fun CacheService.invalidateTaskComments(taskId: UUID) {
    val key = CacheKeys.taskCommentsKey(taskId.toString())
    invalidateCache(key, "comments for task $taskId")
}