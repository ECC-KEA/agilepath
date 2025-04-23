package dev.ecckea.agilepath.backend.config

import dev.ecckea.agilepath.backend.support.TestSecurityContextFilter
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.cache.CacheManager
import org.springframework.cache.support.NoOpCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.core.Ordered
import org.springframework.security.oauth2.jwt.BadJwtException
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import java.time.Instant

@TestConfiguration
@Profile("test")
@Import(TestSecurityConfig::class)
class TestAppConfig {

    @Bean
    @Primary
    fun testJwtDecoder(): JwtDecoder = JwtDecoder { token ->
        if (token != "test-token") throw BadJwtException("Invalid test token")

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

    @Bean
    @Primary
    fun testCacheManager(): CacheManager = NoOpCacheManager()

    @Bean
    @Primary
    fun testSecurityContextFilterRegistration(jwtDecoder: JwtDecoder): FilterRegistrationBean<TestSecurityContextFilter> {
        val registration = FilterRegistrationBean(TestSecurityContextFilter(jwtDecoder))
        registration.order = Ordered.HIGHEST_PRECEDENCE
        return registration
    }
}
