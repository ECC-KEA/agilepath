package dev.ecckea.agilepath.backend.infrastructure.cache

import com.fasterxml.jackson.core.type.TypeReference
import dev.ecckea.agilepath.backend.domain.story.model.Story
import java.util.*

/**
 * Story domain-specific extension functions for the CacheService.
 * These functions provide caching operations for individual stories and project-related stories.
 */

/**
 * Retrieves a story from the cache by its ID.
 *
 * @param id The unique identifier of the story.
 * @return The cached Story object, or null if not found.
 */
fun CacheService.getStory(id: UUID): Story? {
    val key = CacheKeys.storyKey(id.toString())
    return getFromCache(key, Story::class.java, "story $id")
}

/**
 * Caches a story with a specified time-to-live (TTL).
 *
 * @param story The Story object to cache.
 * @param ttlMinutes The time-to-live for the cached story in minutes. Defaults to 15 minutes.
 */
fun CacheService.cacheStory(story: Story, ttlMinutes: Long = 15) {
    val key = CacheKeys.storyKey(story.id.toString())
    setInCache(key, story, ttlMinutes, "story ${story.id}")
}

/**
 * Invalidates a cached story by its ID.
 *
 * @param id The unique identifier of the story to invalidate.
 */
fun CacheService.invalidateStory(id: UUID) {
    val key = CacheKeys.storyKey(id.toString())
    invalidateCache(key, "story $id")
}

/**
 * Project stories related extensions.
 */

/**
 * Retrieves a list of stories associated with a specific project from the cache.
 *
 * @param projectId The unique identifier of the project.
 * @return A list of cached Story objects, or null if not found.
 */
fun CacheService.getProjectStories(projectId: UUID): List<Story>? {
    val key = CacheKeys.projectStoriesKey(projectId.toString())
    return getListFromCache(key, object : TypeReference<List<Story>>() {}, "stories for project $projectId")
}

/**
 * Caches a list of stories for a specific project with a specified TTL.
 *
 * @param projectId The unique identifier of the project.
 * @param stories The list of Story objects to cache.
 * @param ttlMinutes The time-to-live for the cached stories in minutes. Defaults to 15 minutes.
 */
fun CacheService.cacheProjectStories(projectId: UUID, stories: List<Story>, ttlMinutes: Long = 15) {
    val key = CacheKeys.projectStoriesKey(projectId.toString())
    setInCache(key, stories, ttlMinutes, "stories for project $projectId")
}

/**
 * Invalidates cached stories for a specific project by its ID.
 *
 * @param projectId The unique identifier of the project.
 */
fun CacheService.invalidateProjectStories(projectId: UUID) {
    val key = CacheKeys.projectStoriesKey(projectId.toString())
    invalidateCache(key, "stories for project $projectId")
}