package dev.ecckea.agilepath.backend.config

import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.cache.Cache
import org.springframework.cache.interceptor.CacheErrorHandler
import org.springframework.cache.interceptor.SimpleCacheErrorHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

/**
 * Configuration class for Redis cache setup in the application.
 *
 * This class configures Redis as the caching provider and sets up error handling
 * for cache operations. It is active in all profiles except the "test" profile,
 * where an alternative caching solution may be used.
 */
@Configuration
@Profile("!test")
class CacheConfig : Logged() {

    /**
     * Configures a Redis template for string key-value operations.
     *
     * Sets up a RedisTemplate that uses StringRedisSerializer for all serialization needs,
     * making it suitable for simple string-based caching with readable keys and values in Redis.
     *
     * @param connectionFactory The Redis connection factory to use
     * @return A configured RedisTemplate instance for string operations
     */
    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, String> {
        val template = RedisTemplate<String, String>()
        template.connectionFactory = connectionFactory
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = StringRedisSerializer()
        template.hashKeySerializer = StringRedisSerializer()
        template.hashValueSerializer = StringRedisSerializer()
        template.afterPropertiesSet()
        return template
    }

    /**
     * Provides a cache error handler with enhanced logging.
     *
     * Extends the SimpleCacheErrorHandler to add detailed error logging for all cache operations
     * while maintaining the default error handling behavior. This helps with troubleshooting
     * cache-related issues in production environments.
     *
     * @return A custom CacheErrorHandler implementation with logging
     */
    @Bean
    fun cacheErrorHandler(): CacheErrorHandler {
        return object : SimpleCacheErrorHandler() {
            /**
             * Handles errors during cache retrieval operations.
             *
             * Logs the error details including the cache name and key before delegating
             * to the parent handler.
             *
             * @param exception The runtime exception that occurred
             * @param cache The cache where the error occurred
             * @param key The key being accessed when the error occurred
             */
            override fun handleCacheGetError(exception: RuntimeException, cache: Cache, key: Any) {
                log.error("Cache get error for key $key in cache ${cache.name}", exception)
                super.handleCacheGetError(exception, cache, key)
            }

            /**
             * Handles errors during cache update operations.
             *
             * Logs the error details including the cache name, key, and value before
             * delegating to the parent handler.
             *
             * @param exception The runtime exception that occurred
             * @param cache The cache where the error occurred
             * @param key The key being updated when the error occurred
             * @param value The value being stored when the error occurred
             */
            override fun handleCachePutError(exception: RuntimeException, cache: Cache, key: Any, value: Any?) {
                log.error("Cache put error for key $key in cache ${cache.name}", exception)
                super.handleCachePutError(exception, cache, key, value)
            }

            /**
             * Handles errors during cache entry removal operations.
             *
             * Logs the error details including the cache name and key before
             * delegating to the parent handler.
             *
             * @param exception The runtime exception that occurred
             * @param cache The cache where the error occurred
             * @param key The key being removed when the error occurred
             */
            override fun handleCacheEvictError(exception: RuntimeException, cache: Cache, key: Any) {
                log.error("Cache evict error for key $key in cache ${cache.name}", exception)
                super.handleCacheEvictError(exception, cache, key)
            }

            /**
             * Handles errors during complete cache clearing operations.
             *
             * Logs the error details including the cache name before
             * delegating to the parent handler.
             *
             * @param exception The runtime exception that occurred
             * @param cache The cache being cleared when the error occurred
             */
            override fun handleCacheClearError(exception: RuntimeException, cache: Cache) {
                log.error("Cache clear error for cache ${cache.name}", exception)
                super.handleCacheClearError(exception, cache)
            }
        }
    }
}
