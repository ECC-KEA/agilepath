package dev.ecckea.agilepath.backend.infrastructure.cache

import com.fasterxml.jackson.core.type.TypeReference
import dev.ecckea.agilepath.backend.domain.column.model.SprintColumn
import java.util.*

/**
 * Sprint column domain-specific extension functions for the CacheService.
 */

fun CacheService.getSprintColumn(id: UUID): SprintColumn? {
    val key = CacheKeys.sprintColumnKey(id.toString())
    return getFromCache(key, SprintColumn::class.java, "sprint column $id")
}

fun CacheService.cacheSprintColumn(column: SprintColumn, ttlMinutes: Long = 15) {
    val key = CacheKeys.sprintColumnKey(column.id.toString())
    setInCache(key, column, ttlMinutes, "sprint column ${column.id}")
}

fun CacheService.invalidateSprintColumn(id: UUID) {
    val key = CacheKeys.sprintColumnKey(id.toString())
    invalidateCache(key, "sprint column $id")
}

/**
 * Sprint columns related extensions
 */
fun CacheService.getSprintColumns(sprintId: UUID): List<SprintColumn>? {
    val key = CacheKeys.sprintColumnsKey(sprintId.toString())
    return getListFromCache(key, object : TypeReference<List<SprintColumn>>() {}, "columns for sprint $sprintId")
}

fun CacheService.cacheSprintColumns(sprintId: UUID, columns: List<SprintColumn>, ttlMinutes: Long = 15) {
    val key = CacheKeys.sprintColumnsKey(sprintId.toString())
    setInCache(key, columns, ttlMinutes, "columns for sprint $sprintId")
}

fun CacheService.invalidateSprintColumns(sprintId: UUID) {
    val key = CacheKeys.sprintColumnsKey(sprintId.toString())
    invalidateCache(key, "columns for sprint $sprintId")
}