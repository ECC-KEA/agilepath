package dev.ecckea.agilepath.backend.config

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.SimpleCacheErrorHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import java.time.Duration

/**
 * Configures Spring Cache using Redis as the underlying cache backend.
 *
 * While this class currently relies on RedisCacheManager and related Redis-specific
 * imports, we consider this an acceptable implementation detail leakage within the
 * configuration layer.
 *
 * The setup is designed to be easily replaceable with alternative caching providers
 * like Caffeine or EhCache, should requirements change in the future.
 */
@Configuration
@EnableCaching
class CacheConfig {
    /**
     * Configures the primary CacheManager bean using Redis as the backing store.
     *
     * Sets a default TTL of 15 minutes and serializes cache values as JSON.
     * Used by Spring's @Cacheable / @CacheEvict annotations.
     */
    @Bean
    fun cacheManager(connectionFactory: RedisConnectionFactory): CacheManager {
        val config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(15)) // default TTL
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer())
            )

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .build()
    }

    @Bean
    fun cacheErrorHandler(): SimpleCacheErrorHandler = SimpleCacheErrorHandler()

    /**
     * Provides a RedisTemplate with JSON serialization for values and string keys,
     * used for direct Redis access outside of Spring Cache abstraction.
     */
    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        return RedisTemplate<String, Any>().apply {
            setConnectionFactory(connectionFactory)
            keySerializer = org.springframework.data.redis.serializer.StringRedisSerializer()
            valueSerializer = GenericJackson2JsonRedisSerializer()
            hashKeySerializer = org.springframework.data.redis.serializer.StringRedisSerializer()
            hashValueSerializer = GenericJackson2JsonRedisSerializer()
            afterPropertiesSet()
        }
    }
}