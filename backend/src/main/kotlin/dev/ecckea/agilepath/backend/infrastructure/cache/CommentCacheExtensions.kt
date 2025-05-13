package dev.ecckea.agilepath.backend.infrastructure.cache

import com.fasterxml.jackson.core.type.TypeReference
import dev.ecckea.agilepath.backend.domain.story.model.Comment
import java.util.*

/**
 * Comment domain-specific extension functions for the CacheService.
 */

fun CacheService.getComment(id: UUID): Comment? {
    val key = CacheKeys.commentKey(id.toString())
    return getFromCache(key, Comment::class.java, "comment $id")
}

fun CacheService.cacheComment(comment: Comment, ttlMinutes: Long = 15) {
    val key = CacheKeys.commentKey(comment.id.toString())
    setInCache(key, comment, ttlMinutes, "comment ${comment.id}")
}

fun CacheService.invalidateComment(id: UUID) {
    val key = CacheKeys.commentKey(id.toString())
    invalidateCache(key, "comment $id")
}

/**
 * Story comments related extensions
 */
fun CacheService.getStoryComments(storyId: UUID): List<Comment>? {
    val key = CacheKeys.storyCommentsKey(storyId.toString())
    return getListFromCache(key, object : TypeReference<List<Comment>>() {}, "comments for story $storyId")
}

fun CacheService.cacheStoryComments(storyId: UUID, comments: List<Comment>, ttlMinutes: Long = 15) {
    val key = CacheKeys.storyCommentsKey(storyId.toString())
    setInCache(key, comments, ttlMinutes, "comments for story $storyId")
}

fun CacheService.invalidateStoryComments(storyId: UUID) {
    val key = CacheKeys.storyCommentsKey(storyId.toString())
    invalidateCache(key, "comments for story $storyId")
}

/**
 * Task comments related extensions
 */
fun CacheService.getTaskComments(taskId: UUID): List<Comment>? {
    val key = CacheKeys.taskCommentsKey(taskId.toString())
    return getListFromCache(key, object : TypeReference<List<Comment>>() {}, "comments for task $taskId")
}

fun CacheService.cacheTaskComments(taskId: UUID, comments: List<Comment>, ttlMinutes: Long = 15) {
    val key = CacheKeys.taskCommentsKey(taskId.toString())
    setInCache(key, comments, ttlMinutes, "comments for task $taskId")
}

fun CacheService.invalidateTaskComments(taskId: UUID) {
    val key = CacheKeys.taskCommentsKey(taskId.toString())
    invalidateCache(key, "comments for task $taskId")
}