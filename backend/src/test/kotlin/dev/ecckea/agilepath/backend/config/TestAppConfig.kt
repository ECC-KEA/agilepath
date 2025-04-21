package dev.ecckea.agilepath.backend.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.cache.CacheManager
import org.springframework.cache.support.NoOpCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.security.oauth2.jwt.BadJwtException
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import java.time.Instant

@TestConfiguration
@Profile("test") // ✅ only loaded when test profile is active
class TestAppConfig {

    /**
     * Provide a mocked JwtDecoder that always returns a valid user with known claims.
     */
    @Bean
    @Primary
    fun testJwtDecoder(): JwtDecoder {
        return JwtDecoder { token ->
            if (token != "test-token") {
                throw BadJwtException("Invalid test token")
            }
            val claims = mapOf(
                "sub" to "user-id",
                "email" to "test@example.com",
                "username" to "testuser",
                "name" to "Test User",
                "image_url" to "http://test"
            )

            Jwt(
                token,
                Instant.now(),
                Instant.now().plusSeconds(3600),
                mapOf("alg" to "RS256"),
                claims
            )
        }
    }

    /**
     * Disable Redis caching entirely during tests.
     */
    @Bean(name = ["testCacheManager"])
    @Primary
    fun cacheManager(): CacheManager = NoOpCacheManager()
}