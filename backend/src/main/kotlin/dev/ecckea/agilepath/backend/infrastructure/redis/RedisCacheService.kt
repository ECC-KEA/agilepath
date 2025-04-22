package dev.ecckea.agilepath.backend.infrastructure.redis

import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
@Profile("!test")
class RedisCacheService(
    private val redisTemplate: RedisTemplate<String, Any>
) {
    @Suppress("S6518") // False positive: This rule encourages using indexed access operators (e.g., []),
    // but RedisTemplate's get() method is part of a third-party API (Spring Data Redis)
    // and does not support Kotlin's operator overloading. Therefore, using get() here is correct and intentional.
    fun get(key: String): Any? = redisTemplate.opsForValue().get(key)
    @Suppress("S6518") // False positive: RedisTemplate does not support indexed access; using get() is intentional.
    fun set(key: String, value: Any, ttlMinutes: Long = 15) {
        redisTemplate.opsForValue().set(key, value, ttlMinutes, TimeUnit.MINUTES)
    }

    fun delete(key: String) = redisTemplate.delete(key)
}