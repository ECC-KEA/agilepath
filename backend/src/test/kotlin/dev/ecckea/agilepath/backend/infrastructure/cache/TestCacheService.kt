package dev.ecckea.agilepath.backend.infrastructure.cache

import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

/**
 * Test implementation of CacheService that uses MockRedisCacheService.
 */
@Component
@Profile("test")
@Primary
class TestCacheService(
    private val mockRedisCache: MockRedisCacheService
) : CacheService(mockRedisCache) {

    fun clearAllCaches() {
        mockRedisCache.clearCache()
    }
}