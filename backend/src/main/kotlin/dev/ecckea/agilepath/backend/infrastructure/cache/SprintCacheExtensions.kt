package dev.ecckea.agilepath.backend.infrastructure.cache

import com.fasterxml.jackson.core.type.TypeReference
import dev.ecckea.agilepath.backend.domain.sprint.model.Sprint
import java.util.*

/**
 * Sprint domain-specific extension functions for the CacheService.
 */

fun CacheService.getSprint(id: UUID): Sprint? {
    val key = CacheKeys.sprintKey(id.toString())
    return getFromCache(key, Sprint::class.java, "sprint $id")
}

fun CacheService.cacheSprint(sprint: Sprint, ttlMinutes: Long = 15) {
    val key = CacheKeys.sprintKey(sprint.id.toString())
    setInCache(key, sprint, ttlMinutes, "sprint ${sprint.id}")
}

fun CacheService.invalidateSprint(id: UUID) {
    val key = CacheKeys.sprintKey(id.toString())
    invalidateCache(key, "sprint $id")
}

/**
 * Project sprints related extensions
 */
fun CacheService.getProjectSprints(projectId: UUID): List<Sprint>? {
    val key = CacheKeys.projectSprintsKey(projectId.toString())
    return getListFromCache(key, object : TypeReference<List<Sprint>>() {}, "sprints for project $projectId")
}

fun CacheService.cacheProjectSprints(projectId: UUID, sprints: List<Sprint>, ttlMinutes: Long = 15) {
    val key = CacheKeys.projectSprintsKey(projectId.toString())
    setInCache(key, sprints, ttlMinutes, "sprints for project $projectId")
}

fun CacheService.invalidateProjectSprints(projectId: UUID) {
    val key = CacheKeys.projectSprintsKey(projectId.toString())
    invalidateCache(key, "sprints for project $projectId")
}