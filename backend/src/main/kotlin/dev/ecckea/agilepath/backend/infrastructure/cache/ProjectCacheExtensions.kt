package dev.ecckea.agilepath.backend.infrastructure.cache

import com.fasterxml.jackson.core.type.TypeReference
import dev.ecckea.agilepath.backend.domain.project.model.Project
import java.util.*

/**
 * Project domain-specific extension functions for the CacheService.
 */

fun CacheService.getProject(id: UUID): Project? {
    val key = CacheKeys.projectKey(id.toString())
    return getFromCache(key, Project::class.java, "project $id")
}

fun CacheService.cacheProject(project: Project, ttlMinutes: Long = 15) {
    val key = CacheKeys.projectKey(project.id.toString())
    setInCache(key, project, ttlMinutes, "project ${project.id}")
}

fun CacheService.invalidateProject(id: UUID) {
    val key = CacheKeys.projectKey(id.toString())
    invalidateCache(key, "project $id")
}

/**
 * User projects related extensions
 */
fun CacheService.getUserProjects(userId: String): List<Project>? {
    val key = CacheKeys.userProjectsKey(userId)
    return getListFromCache(key, object : TypeReference<List<Project>>() {}, "projects for user $userId")
}

fun CacheService.cacheUserProjects(userId: String, projects: List<Project>, ttlMinutes: Long = 15) {
    val key = CacheKeys.userProjectsKey(userId)
    setInCache(key, projects, ttlMinutes, "projects for user $userId")
}

fun CacheService.invalidateUserProjects(userId: String) {
    val key = CacheKeys.userProjectsKey(userId)
    invalidateCache(key, "projects for user $userId")
}