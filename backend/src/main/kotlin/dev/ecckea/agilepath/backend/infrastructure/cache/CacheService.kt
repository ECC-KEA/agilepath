package dev.ecckea.agilepath.backend.infrastructure.cache

import com.fasterxml.jackson.core.type.TypeReference
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

/**
 * Service that provides a logging wrapper around Redis cache operations.
 *
 * This service enhances the underlying [RedisCacheService] by adding consistent
 * logging for all cache operations (hits, misses, storage, and invalidation),
 * making it easier to track and debug cache behavior in the application.
 *
 * Not active in the "test" profile to allow for different caching strategies
 * during testing.
 */
@Component
@Profile("!test")
class CacheService(
    private val redisCache: RedisCacheService,
) : Logged() {

    /**
     * Generic method to retrieve a single object from cache with hit/miss logging.
     *
     * @param key Cache key for the object
     * @param clazz Type of object to retrieve
     * @param idForLogging Descriptive identifier used in logs
     * @return The cached object or null if not found
     */

    fun <T> getFromCache(key: String, clazz: Class<T>, idForLogging: String): T? {
        return redisCache.get(key, clazz)?.also {
            log.info("Cache hit for $idForLogging")
        } ?: run {
            log.info("Cache miss for $idForLogging")
            null
        }
    }

    /**
     * Generic method to retrieve a list from cache with hit/miss logging.
     *
     * @param key The cache key
     * @param typeRef The type reference for the list
     * @param idForLogging An identifier used in log messages
     * @return The cached list if found, null otherwise
     */
    fun <T> getListFromCache(key: String, typeRef: TypeReference<T>, idForLogging: String): T? {
        return redisCache.getList<T>(key, typeRef)?.also {
            log.info("Cache hit for $idForLogging")
        } ?: run {
            log.info("Cache miss for $idForLogging")
            null
        }
    }

    /**
     * Generic method to store an item in cache with logging.
     *
     * @param key The cache key
     * @param value The value to cache
     * @param ttlMinutes Time-to-live in minutes
     * @param idForLogging An identifier used in log messages
     */
    fun setInCache(key: String, value: Any, ttlMinutes: Long = 15, idForLogging: String) {
        log.info("Caching $idForLogging with TTL $ttlMinutes minutes")
        redisCache.set(key, value, ttlMinutes)
    }

    /**
     * Generic method to invalidate a cache entry with logging.
     *
     * @param key The cache key
     * @param idForLogging An identifier used in log messages
     */
    fun invalidateCache(key: String, idForLogging: String) {
        log.info("Invalidating cache for $idForLogging")
        redisCache.delete(key)
    }

    /**
     * Generic method to invalidate multiple cache entries with logging.
     *
     * @param pattern The cache key pattern
     * @param description A description for logging
     */
    fun invalidateCachePattern(pattern: String, description: String) {
        log.info("Invalidating cache entries for $description")
        redisCache.deleteByPattern(pattern)
    }
}
