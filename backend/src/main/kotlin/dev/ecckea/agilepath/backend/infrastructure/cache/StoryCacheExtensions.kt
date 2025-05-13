package dev.ecckea.agilepath.backend.infrastructure.cache

import com.fasterxml.jackson.core.type.TypeReference
import dev.ecckea.agilepath.backend.domain.story.model.Story
import java.util.*

/**
 * Story domain-specific extension functions for the CacheService.
 */

fun CacheService.getStory(id: UUID): Story? {
    val key = CacheKeys.storyKey(id.toString())
    return getFromCache(key, Story::class.java, "story $id")
}

fun CacheService.cacheStory(story: Story, ttlMinutes: Long = 15) {
    val key = CacheKeys.storyKey(story.id.toString())
    setInCache(key, story, ttlMinutes, "story ${story.id}")
}

fun CacheService.invalidateStory(id: UUID) {
    val key = CacheKeys.storyKey(id.toString())
    invalidateCache(key, "story $id")
}

/**
 * Project stories related extensions
 */
fun CacheService.getProjectStories(projectId: UUID): List<Story>? {
    val key = CacheKeys.projectStoriesKey(projectId.toString())
    return getListFromCache(key, object : TypeReference<List<Story>>() {}, "stories for project $projectId")
}

fun CacheService.cacheProjectStories(projectId: UUID, stories: List<Story>, ttlMinutes: Long = 15) {
    val key = CacheKeys.projectStoriesKey(projectId.toString())
    setInCache(key, stories, ttlMinutes, "stories for project $projectId")
}

fun CacheService.invalidateProjectStories(projectId: UUID) {
    val key = CacheKeys.projectStoriesKey(projectId.toString())
    invalidateCache(key, "stories for project $projectId")
}