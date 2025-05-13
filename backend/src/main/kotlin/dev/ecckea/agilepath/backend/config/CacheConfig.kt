package dev.ecckea.agilepath.backend.config

import com.fasterxml.jackson.databind.ObjectMapper
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.Cache
import org.springframework.cache.interceptor.CacheErrorHandler
import org.springframework.cache.interceptor.SimpleCacheErrorHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
@Profile("!test")
class CacheConfig(
    @Qualifier("redisCacheObjectMapper") private val redisCacheObjectMapper: ObjectMapper,
) : Logged() {

    /**
     * Configures a RedisTemplate bean for direct Redis operations.
     * Used by the RedisCacheService for explicit cache management.
     */
    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        val serializer = GenericJackson2JsonRedisSerializer(redisCacheObjectMapper)

        template.connectionFactory = connectionFactory
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = serializer
        template.hashKeySerializer = StringRedisSerializer()
        template.hashValueSerializer = serializer
        template.afterPropertiesSet()
        return template
    }

    /**
     * Provides error handling for cache operations.
     */
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
}