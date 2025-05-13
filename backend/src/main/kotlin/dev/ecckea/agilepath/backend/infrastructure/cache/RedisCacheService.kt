package dev.ecckea.agilepath.backend.infrastructure.cache

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

/**
 * Service for interacting with Redis cache.
 * Provides methods to get, set, and delete cache entries.
 *
 * @property redisTemplate The RedisTemplate used for Redis operations.
 * @property objectMapper The ObjectMapper used for serialization and deserialization.
 */
@Component
@Profile("!test")
class RedisCacheService(
    private val redisTemplate: RedisTemplate<String, Any>,
    @Qualifier("redisCacheObjectMapper") private val objectMapper: ObjectMapper
) {

    /**
     * Retrieves a value from Redis and converts it to the specified class type.
     *
     * @param key The key of the cached value.
     * @param clazz The class type to convert the cached value to.
     * @return The cached value converted to the specified type, or null if the key does not exist.
     */
    fun <T> get(key: String, clazz: Class<T>): T? {
        val raw = redisTemplate.opsForValue().get(key) ?: return null
        return objectMapper.convertValue(raw, clazz)
    }

    /**
     * Retrieves a list or collection from Redis and converts it to the specified type.
     *
     * @param key The key of the cached value.
     * @param typeRef The TypeReference representing the type to convert the cached value to.
     * @return The cached value converted to the specified type, or null if the key does not exist.
     */
    fun <T> getList(key: String, typeRef: TypeReference<T>): T? {
        val raw = redisTemplate.opsForValue().get(key) ?: return null
        val javaType = objectMapper.typeFactory.constructType(typeRef)
        val json = objectMapper.writeValueAsString(raw)
        return objectMapper.readValue(json, javaType)
    }

    /**
     * Stores a value in Redis with a specified time-to-live (TTL).
     *
     * @param key The key to store the value under.
     * @param value The value to store in the cache.
     * @param ttlMinutes The time-to-live for the cached value in minutes. Defaults to 15 minutes.
     */
    fun set(key: String, value: Any, ttlMinutes: Long = 15) {
        redisTemplate.opsForValue().set(key, value, ttlMinutes, TimeUnit.MINUTES)
    }

    /**
     * Deletes a value from Redis by its key.
     *
     * @param key The key of the value to delete.
     */
    fun delete(key: String) {
        redisTemplate.delete(key)
    }

    /**
     * Deletes all keys matching a given pattern from Redis.
     *
     * @param pattern The pattern to match keys against.
     */
    fun deleteByPattern(pattern: String) {
        val keys = redisTemplate.keys("$pattern*")
        if (keys.isNotEmpty()) {
            redisTemplate.delete(keys)
        }
    }
}