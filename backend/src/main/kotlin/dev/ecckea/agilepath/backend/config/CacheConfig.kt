package dev.ecckea.agilepath.backend.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
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
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

/**
 * Configures Spring Cache using Redis as the underlying cache backend.
 */
@Configuration
//@EnableCaching
class CacheConfig {

    /**
     * Creates and configures a custom ObjectMapper instance.
     *
     * This method sets up the ObjectMapper with the following modules:
     * - JavaTimeModule for handling Java 8 date and time types such as ZonedDateTime and Instant.
     * - KotlinModule for better Kotlin support, including handling of Kotlin-specific features.
     * - Automatically registers any additional modules found on the classpath.
     *
     * @return a configured instance of ObjectMapper.
     */
    private fun customObjectMapper(): ObjectMapper =
        ObjectMapper()
            .registerModule(JavaTimeModule()) // for ZonedDateTime, Instant etc.
            .registerModule(KotlinModule.Builder().build())
            .findAndRegisterModules()
    

    @Bean
    fun cacheManager(connectionFactory: RedisConnectionFactory): CacheManager {
        val mapper = customObjectMapper()
        val serializer = GenericJackson2JsonRedisSerializer(mapper)

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
    fun cacheErrorHandler(): SimpleCacheErrorHandler = SimpleCacheErrorHandler()

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
        val mapper = customObjectMapper()
        val serializer = GenericJackson2JsonRedisSerializer(mapper)

        template.connectionFactory = connectionFactory
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = serializer
        template.hashKeySerializer = StringRedisSerializer()
        template.hashValueSerializer = serializer
        template.afterPropertiesSet()
        return template
    }
}