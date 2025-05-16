package dev.ecckea.agilepath.backend.infrastructure.cache

import dev.ecckea.agilepath.backend.domain.user.model.User

/**
 * User domain-specific extension functions for the CacheService.
 * These functions provide caching operations for individual users.
 */

/**
 * Retrieves a user from the cache by their ID.
 *
 * @param id The unique identifier of the user.
 * @return The cached User object, or null if not found.
 */
fun CacheService.getUser(id: String): User? {
    val key = CacheKeys.userKey(id)
    return getFromCache(key, User::class.java, "user $id")
}

/**
 * Caches a user with a specified time-to-live (TTL).
 *
 * @param user The User object to cache.
 * @param ttlMinutes The time-to-live for the cached user in minutes. Defaults to 15 minutes.
 */
fun CacheService.cacheUser(user: User, ttlMinutes: Long = 15) {
    val key = CacheKeys.userKey(user.id)
    setInCache(key, user, ttlMinutes, "user ${user.id}")
}

/**
 * Invalidates a cached user by their ID.
 *
 * @param id The unique identifier of the user to invalidate.
 */
fun CacheService.invalidateUser(id: String) {
    val key = CacheKeys.userKey(id)
    invalidateCache(key, "user $id")
}