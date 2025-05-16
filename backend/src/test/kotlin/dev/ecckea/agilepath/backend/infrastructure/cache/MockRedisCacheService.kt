package dev.ecckea.agilepath.backend.infrastructure.cache

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
@Profile("test")
@Primary
class MockRedisCacheService(
    private val objectMapper: ObjectMapper
) : RedisCacheService(DummyRedisTemplate(), objectMapper) {

    private val cache = ConcurrentHashMap<String, String>()

    override fun <T> get(key: String, clazz: Class<T>): T? {
        val json = cache[key] ?: return null
        return objectMapper.readValue(json, clazz)
    }

    override fun <T> getList(key: String, typeRef: TypeReference<T>): T? {
        val json = cache[key] ?: return null
        val javaType = objectMapper.typeFactory.constructType(typeRef)
        return objectMapper.readValue(json, javaType)
    }

    override fun set(key: String, value: Any, ttlMinutes: Long) {
        val json = objectMapper.writeValueAsString(value)
        cache[key] = json
        // TTL is ignored in test/mock
    }

    override fun delete(key: String) {
        cache.remove(key)
    }

    override fun deleteByPattern(pattern: String) {
        val keysToRemove = cache.keys.filter { it.startsWith(pattern) }
        keysToRemove.forEach { cache.remove(it) }
    }

    fun clearCache() {
        cache.clear()
    }
}

/**
 * Dummy RedisTemplate, never used because all Redis operations are mocked.
 */
class DummyRedisTemplate : RedisTemplate<String, String>()
