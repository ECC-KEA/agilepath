package dev.ecckea.agilepath.backend.config

import com.fasterxml.jackson.databind.ObjectMapper
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.CacheErrorHandler
import org.springframework.cache.interceptor.SimpleCacheErrorHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

/**
 * Configures Spring Cache using Redis as the underlying cache backend.
 */
@Configuration
@EnableCaching
class CacheConfig(
    private val customObjectMapper: ObjectMapper,
) : Logged() {



    /**
     * Configures the primary CacheManager bean using Redis as the backing store.
     *
     * This method sets up a RedisCacheManager with the following configurations:
     * - A default Time-To-Live (TTL) of 15 minutes for cache entries.
     * - String serialization for cache keys.
     * - JSON serialization for cache values using a custom ObjectMapper.
     *
     * @param connectionFactory the RedisConnectionFactory used to establish connections to the Redis server.
     * @return a configured instance of CacheManager for managing application caches.
     */
    @Bean
    fun cacheManager(connectionFactory: RedisConnectionFactory): CacheManager {
        val serializer = GenericJackson2JsonRedisSerializer(customObjectMapper)

        val config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(15))
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer())
            )
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(serializer)
            )

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .build()
    }

    @Bean
    fun cacheErrorHandler(): CacheErrorHandler {
        return object : SimpleCacheErrorHandler() {
            override fun handleCacheGetError(
                exception: RuntimeException,
                cache: Cache,
                key: Any
            ) {
                log.error("Cache get error for key $key in cache ${cache.name}", exception)
                super.handleCacheGetError(exception, cache, key)
            }

            override fun handleCachePutError(
                exception: RuntimeException,
                cache: Cache,
                key: Any,
                value: Any?
            ) {
                log.error("Cache put error for key $key in cache ${cache.name}", exception)
                super.handleCachePutError(exception, cache, key, value)
            }

            override fun handleCacheEvictError(
                exception: RuntimeException,
                cache: Cache,
                key: Any
            ) {
                log.error("Cache evict error for key $key in cache ${cache.name}", exception)
                super.handleCacheEvictError(exception, cache, key)
            }

            override fun handleCacheClearError(
                exception: RuntimeException,
                cache: Cache
            ) {
                log.error("Cache clear error for cache ${cache.name}", exception)
                super.handleCacheClearError(exception, cache)
            }
        }
    }

    /**
     * Configures a RedisTemplate bean for direct access to Redis.
     *
     * This method sets up a RedisTemplate with the following configurations:
     * - String serialization for keys and hash keys.
     * - JSON serialization for values and hash values using a custom ObjectMapper.
     * - The provided RedisConnectionFactory is used to establish connections to the Redis server.
     *
     * @param connectionFactory the RedisConnectionFactory used to connect to the Redis server.
     * @return a configured instance of RedisTemplate for interacting with Redis.
     */
    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        val serializer = GenericJackson2JsonRedisSerializer(customObjectMapper)

        template.connectionFactory = connectionFactory
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = serializer
        template.hashKeySerializer = StringRedisSerializer()
        template.hashValueSerializer = serializer
        template.afterPropertiesSet()
        return template
    }
}