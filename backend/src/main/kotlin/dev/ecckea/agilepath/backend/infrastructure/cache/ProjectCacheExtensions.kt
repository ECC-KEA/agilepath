package dev.ecckea.agilepath.backend.infrastructure.cache

import com.fasterxml.jackson.core.type.TypeReference
import dev.ecckea.agilepath.backend.domain.project.model.Project
import java.util.*

/**
 * Project domain-specific extension functions for the CacheService.
 * These functions provide caching operations for individual projects and user-related projects.
 */

/**
 * Retrieves a project from the cache by its ID.
 *
 * @param id The unique identifier of the project.
 * @return The cached Project object, or null if not found.
 */
fun CacheService.getProject(id: UUID): Project? {
    val key = CacheKeys.projectKey(id.toString())
    return getFromCache(key, Project::class.java, "project $id")
}

/**
 * Caches a project with a specified time-to-live (TTL).
 *
 * @param project The Project object to cache.
 * @param ttlMinutes The time-to-live for the cached project in minutes. Defaults to 15 minutes.
 */
fun CacheService.cacheProject(project: Project, ttlMinutes: Long = 15) {
    val key = CacheKeys.projectKey(project.id.toString())
    setInCache(key, project, ttlMinutes, "project ${project.id}")
}

/**
 * Invalidates a cached project by its ID.
 *
 * @param id The unique identifier of the project to invalidate.
 */
fun CacheService.invalidateProject(id: UUID) {
    val key = CacheKeys.projectKey(id.toString())
    invalidateCache(key, "project $id")
}

/**
 * Retrieves a list of projects associated with a specific user from the cache.
 *
 * @param userId The unique identifier of the user.
 * @return A list of cached Project objects, or null if not found.
 */
fun CacheService.getUserProjects(userId: String): List<Project>? {
    val key = CacheKeys.userProjectsKey(userId)
    return getListFromCache(key, object : TypeReference<List<Project>>() {}, "projects for user $userId")
}

/**
 * Caches a list of projects for a specific user with a specified TTL.
 *
 * @param userId The unique identifier of the user.
 * @param projects The list of Project objects to cache.
 * @param ttlMinutes The time-to-live for the cached projects in minutes. Defaults to 15 minutes.
 */
fun CacheService.cacheUserProjects(userId: String, projects: List<Project>, ttlMinutes: Long = 15) {
    val key = CacheKeys.userProjectsKey(userId)
    setInCache(key, projects, ttlMinutes, "projects for user $userId")
}

/**
 * Invalidates cached projects for a specific user by their ID.
 *
 * @param userId The unique identifier of the user.
 */
fun CacheService.invalidateUserProjects(userId: String) {
    val key = CacheKeys.userProjectsKey(userId)
    invalidateCache(key, "projects for user $userId")
}