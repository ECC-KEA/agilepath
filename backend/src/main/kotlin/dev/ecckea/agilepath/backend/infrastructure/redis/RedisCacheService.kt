package dev.ecckea.agilepath.backend.infrastructure.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisCacheService(
    private val redisTemplate: RedisTemplate<String, Any>
) {
    fun get(key: String): Any? = redisTemplate.opsForValue().get(key)

    fun set(key: String, value: Any, ttlMinutes: Long = 15) {
        redisTemplate.opsForValue().set(key, value, ttlMinutes, TimeUnit.MINUTES)
    }

    fun delete(key: String) = redisTemplate.delete(key)
}