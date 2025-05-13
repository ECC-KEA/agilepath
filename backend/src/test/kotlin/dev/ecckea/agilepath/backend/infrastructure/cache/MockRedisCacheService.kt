package dev.ecckea.agilepath.backend.infrastructure.cache

import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

/**
 * In-memory implementation of RedisCacheService for testing.
 */
@Component
@Profile("test")
@Primary
class MockRedisCacheService : RedisCacheService(DummyRedisTemplate()) {

    private val cache = ConcurrentHashMap<String, Any>()

    override fun <T> get(key: String): T? {
        @Suppress("UNCHECKED_CAST")
        return cache[key] as? T
    }

    override fun set(key: String, value: Any, ttlMinutes: Long) {
        cache[key] = value
    }

    override fun delete(key: String) {
        cache.remove(key)
    }

    override fun deleteByPattern(pattern: String) {
        val keysToRemove = cache.keys().toList().filter { it.startsWith(pattern) }
        keysToRemove.forEach { cache.remove(it) }
    }

    fun clearCache() {
        cache.clear()
    }
}

/**
 * Dummy implementation of RedisTemplate for testing purposes.
 * None of its methods will be called as the MockRedisCacheService overrides all methods that use it.
 */
class DummyRedisTemplate : RedisTemplate<String, Any>()