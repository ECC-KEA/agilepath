package dev.ecckea.agilepath.backend.infrastructure.cache

import com.fasterxml.jackson.core.type.TypeReference
import dev.ecckea.agilepath.backend.domain.column.model.SprintColumn
import java.util.*

/**
 * Sprint column domain-specific extension functions for the CacheService.
 * These functions provide caching operations for individual sprint columns and sprint-related columns.
 */

/**
 * Retrieves a sprint column from the cache by its ID.
 *
 * @param id The unique identifier of the sprint column.
 * @return The cached SprintColumn object, or null if not found.
 */
fun CacheService.getSprintColumn(id: UUID): SprintColumn? {
    val key = CacheKeys.sprintColumnKey(id.toString())
    return getFromCache(key, SprintColumn::class.java, "sprint column $id")
}

/**
 * Caches a sprint column with a specified time-to-live (TTL).
 *
 * @param column The SprintColumn object to cache.
 * @param ttlMinutes The time-to-live for the cached sprint column in minutes. Defaults to 15 minutes.
 */
fun CacheService.cacheSprintColumn(column: SprintColumn, ttlMinutes: Long = 15) {
    val key = CacheKeys.sprintColumnKey(column.id.toString())
    setInCache(key, column, ttlMinutes, "sprint column ${column.id}")
}

/**
 * Invalidates a cached sprint column by its ID.
 *
 * @param id The unique identifier of the sprint column to invalidate.
 */
fun CacheService.invalidateSprintColumn(id: UUID) {
    val key = CacheKeys.sprintColumnKey(id.toString())
    invalidateCache(key, "sprint column $id")
}

/**
 * Retrieves a list of sprint columns associated with a specific sprint from the cache.
 *
 * @param sprintId The unique identifier of the sprint.
 * @return A list of cached SprintColumn objects, or null if not found.
 */
fun CacheService.getSprintColumns(sprintId: UUID): List<SprintColumn>? {
    val key = CacheKeys.sprintColumnsKey(sprintId.toString())
    return getListFromCache(key, object : TypeReference<List<SprintColumn>>() {}, "columns for sprint $sprintId")
}

/**
 * Caches a list of sprint columns for a specific sprint with a specified TTL.
 *
 * @param sprintId The unique identifier of the sprint.
 * @param columns The list of SprintColumn objects to cache.
 * @param ttlMinutes The time-to-live for the cached sprint columns in minutes. Defaults to 15 minutes.
 */
fun CacheService.cacheSprintColumns(sprintId: UUID, columns: List<SprintColumn>, ttlMinutes: Long = 15) {
    val key = CacheKeys.sprintColumnsKey(sprintId.toString())
    setInCache(key, columns, ttlMinutes, "columns for sprint $sprintId")
}

/**
 * Invalidates cached sprint columns for a specific sprint by its ID.
 *
 * @param sprintId The unique identifier of the sprint.
 */
fun CacheService.invalidateSprintColumns(sprintId: UUID) {
    val key = CacheKeys.sprintColumnsKey(sprintId.toString())
    invalidateCache(key, "columns for sprint $sprintId")
}