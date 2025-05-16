package dev.ecckea.agilepath.backend.infrastructure.cache

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

/**
 * Redis-based caching service that handles JSON serialization.
 *
 * This service provides a simple interface for storing and retrieving
 * JSON-serialized objects in Redis. It handles all serialization/deserialization
 * automatically and supports configurable time-to-live for cached items.
 *
 * Not active in the "test" profile.
 */
@Component
@Profile("!test")
class RedisCacheService(
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {

    /**
     * Retrieves and deserializes a cached object.
     *
     * @param key The cache key
     * @param clazz The class to deserialize the object into
     * @return The deserialized object, or null if not in cache
     */
    fun <T> get(key: String, clazz: Class<T>): T? {
        val json = redisTemplate.opsForValue().get(key) ?: return null
        return objectMapper.readValue(json, clazz)
    }

    /**
     * Retrieves and deserializes a cached list or complex object.
     *
     * @param key The cache key
     * @param typeRef Type reference for handling generic collections
     * @return The deserialized object, or null if not in cache
     */
    fun <T> getList(key: String, typeRef: TypeReference<T>): T? {
        val json = redisTemplate.opsForValue().get(key) ?: return null
        val javaType = objectMapper.typeFactory.constructType(typeRef)
        return objectMapper.readValue(json, javaType)
    }

    /**
     * Serializes and stores an object in the cache.
     *
     * @param key The cache key
     * @param value The object to cache
     * @param ttlMinutes How long to keep in cache (defaults to 15 minutes)
     */
    fun set(key: String, value: Any, ttlMinutes: Long = 15) {
        val json = objectMapper.writeValueAsString(value)
        redisTemplate.opsForValue().set(key, json, ttlMinutes, TimeUnit.MINUTES)
    }

    /**
     * Removes a single item from the cache.
     *
     * @param key The cache key to remove
     */
    fun delete(key: String) {
        redisTemplate.delete(key)
    }

    /**
     * Removes multiple items matching a pattern from the cache.
     *
     * @param pattern The key pattern to match (e.g., "user:123:*")
     */
    fun deleteByPattern(pattern: String) {
        val keys = redisTemplate.keys("$pattern*")
        if (keys.isNotEmpty()) {
            redisTemplate.delete(keys)
        }
    }
}