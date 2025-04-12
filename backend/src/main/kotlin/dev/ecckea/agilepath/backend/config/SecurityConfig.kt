package dev.ecckea.agilepath.backend.config

import dev.ecckea.agilepath.backend.shared.security.UserPrincipal
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain

/**
 * Configures global Spring Security for JWT-based stateless authentication using Clerk.
 *
 * This setup:
 * - Disables CSRF (as this is a stateless API)
 * - Allows public access to Swagger UI and documentation
 * - Protects all other endpoints (requires valid Clerk-issued JWT)
 * - Validates JWTs via Clerk's JWK endpoint using RS256
 * - Maps the JWT into a simple authenticated `Principal`
 */
@Configuration
@EnableMethodSecurity
class SecurityConfig {

    /**
     * Defines the security filter chain with route-specific access rules.
     * - Public: Swagger UI and OpenAPI docs
     * - Protected: All other endpoints require authentication
     * - Uses OAuth2 Resource Server integration for JWT handling
     */
    @Bean
    fun filterChain(http: HttpSecurity, jwtDecoder: JwtDecoder): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**"
                ).permitAll()
                it.anyRequest().authenticated()
            }
            .oauth2ResourceServer {
                it.jwt { config ->
                    config.jwtAuthenticationConverter(::jwtAuthentication)
                }
            }

        return http.build()
    }

    /**
     * Configures the JWT decoder using Clerk's JWK Set URI.
     * Clerk uses RS256 to sign tokens, so we validate using the public key retrieved from their endpoint.
     *
     * @return a configured `JwtDecoder` that fetches Clerk's JWKs
     */
    @Bean
    fun jwtDecoder(
        @Value("\${CLERK_ISSUER}") issuer: String
    ): JwtDecoder {
        return NimbusJwtDecoder.withJwkSetUri("$issuer/.well-known/jwks.json").build()
    }

    /**
     * Converts a validated Clerk JWT into a Spring Security authentication token.
     *
     * This implementation extracts relevant identity claims from the token
     * and maps them to a `UserPrincipal`, which becomes the authenticated principal.
     *
     * The extracted claims include:
     * - `sub` → Clerk user ID (used as the unique identifier)
     * - `email` → User's primary email
     * - `username` → GitHub username (if present)
     * - `name` → Full name
     * - `image_url` → Avatar URL
     *
     * This principal can be accessed throughout the application for user lookups,
     * auditing, and authorization purposes.
     */
    private fun jwtAuthentication(jwt: Jwt): AbstractAuthenticationToken {
        val userPrincipal = UserPrincipal(
            id = jwt.subject,
            email = jwt.getClaimAsString("email"),
            githubUsername = jwt.getClaimAsString("username"),
            fullName = jwt.getClaimAsString("name"),
            avatarUrl = jwt.getClaimAsString("image_url")
        )

        return object : AbstractAuthenticationToken(emptyList()) {
            override fun getCredentials(): Any = jwt.tokenValue
            override fun getPrincipal(): Any = userPrincipal
            override fun isAuthenticated(): Boolean = true
        }
    }
}