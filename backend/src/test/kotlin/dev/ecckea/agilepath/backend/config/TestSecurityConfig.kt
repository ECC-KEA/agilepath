package dev.ecckea.agilepath.backend.config

import dev.ecckea.agilepath.backend.shared.security.CustomAuthenticationEntryPoint
import dev.ecckea.agilepath.backend.shared.security.UserPrincipal
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.web.SecurityFilterChain

@TestConfiguration
@Profile("test")
class TestSecurityConfig {
    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtDecoder: JwtDecoder,
        customAuthenticationEntryPoint: CustomAuthenticationEntryPoint
    ): SecurityFilterChain {
        http
            .cors { }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**"
                ).permitAll()
                it.anyRequest().authenticated()
            }
            .oauth2ResourceServer {
                it.jwt { config ->
                    // Use the same authentication converter as in production
                    config.jwtAuthenticationConverter { jwt ->
                        val userPrincipal = UserPrincipal(
                            id = jwt.subject,
                            email = jwt.getClaimAsString("email"),
                            githubUsername = jwt.getClaimAsString("username"),
                            fullName = jwt.getClaimAsString("name"),
                            avatarUrl = jwt.getClaimAsString("image_url")
                        )

                        object : AbstractAuthenticationToken(emptyList()) {
                            override fun getCredentials(): Any = jwt.tokenValue
                            override fun getPrincipal(): Any = userPrincipal
                            override fun isAuthenticated(): Boolean = true
                        }
                    }
                }
                it.authenticationEntryPoint(customAuthenticationEntryPoint)
            }

        return http.build()
    }
}