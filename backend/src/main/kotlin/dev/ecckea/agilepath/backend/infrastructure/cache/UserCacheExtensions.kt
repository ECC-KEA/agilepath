package dev.ecckea.agilepath.backend.infrastructure.cache

import dev.ecckea.agilepath.backend.domain.user.model.User

/**
 * User domain-specific extension functions for the CacheService.
 */

fun CacheService.getUser(id: String): User? {
    val key = CacheKeys.userKey(id)
    return getFromCache(key, User::class.java, "user $id")
}

fun CacheService.cacheUser(user: User, ttlMinutes: Long = 15) {
    val key = CacheKeys.userKey(user.id)
    setInCache(key, user, ttlMinutes, "user ${user.id}")
}

fun CacheService.invalidateUser(id: String) {
    val key = CacheKeys.userKey(id)
    invalidateCache(key, "user $id")
}