package dev.ecckea.agilepath.backend.infrastructure.cache

import com.fasterxml.jackson.core.type.TypeReference
import dev.ecckea.agilepath.backend.domain.sprint.model.Sprint
import java.util.*

/**
 * Sprint domain-specific extension functions for the CacheService.
 * These functions provide caching operations for individual sprints and project-related sprints.
 */

/**
 * Retrieves a sprint from the cache by its ID.
 *
 * @param id The unique identifier of the sprint.
 * @return The cached Sprint object, or null if not found.
 */
fun CacheService.getSprint(id: UUID): Sprint? {
    val key = CacheKeys.sprintKey(id.toString())
    return getFromCache(key, Sprint::class.java, "sprint $id")
}

/**
 * Caches a sprint with a specified time-to-live (TTL).
 *
 * @param sprint The Sprint object to cache.
 * @param ttlMinutes The time-to-live for the cached sprint in minutes. Defaults to 15 minutes.
 */
fun CacheService.cacheSprint(sprint: Sprint, ttlMinutes: Long = 15) {
    val key = CacheKeys.sprintKey(sprint.id.toString())
    setInCache(key, sprint, ttlMinutes, "sprint ${sprint.id}")
}

/**
 * Invalidates a cached sprint by its ID.
 *
 * @param id The unique identifier of the sprint to invalidate.
 */
fun CacheService.invalidateSprint(id: UUID) {
    val key = CacheKeys.sprintKey(id.toString())
    invalidateCache(key, "sprint $id")
}

/**
 * Retrieves a list of sprints associated with a specific project from the cache.
 *
 * @param projectId The unique identifier of the project.
 * @return A list of cached Sprint objects, or null if not found.
 */
fun CacheService.getProjectSprints(projectId: UUID): List<Sprint>? {
    val key = CacheKeys.projectSprintsKey(projectId.toString())
    return getListFromCache(key, object : TypeReference<List<Sprint>>() {}, "sprints for project $projectId")
}

/**
 * Caches a list of sprints for a specific project with a specified TTL.
 *
 * @param projectId The unique identifier of the project.
 * @param sprints The list of Sprint objects to cache.
 * @param ttlMinutes The time-to-live for the cached sprints in minutes. Defaults to 15 minutes.
 */
fun CacheService.cacheProjectSprints(projectId: UUID, sprints: List<Sprint>, ttlMinutes: Long = 15) {
    val key = CacheKeys.projectSprintsKey(projectId.toString())
    setInCache(key, sprints, ttlMinutes, "sprints for project $projectId")
}

/**
 * Invalidates cached sprints for a specific project by its ID.
 *
 * @param projectId The unique identifier of the project.
 */
fun CacheService.invalidateProjectSprints(projectId: UUID) {
    val key = CacheKeys.projectSprintsKey(projectId.toString())
    invalidateCache(key, "sprints for project $projectId")
}