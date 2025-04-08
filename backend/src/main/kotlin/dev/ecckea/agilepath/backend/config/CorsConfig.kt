package dev.ecckea.agilepath.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig(private val corsProperties: CorsProperties) {

    @Bean
    fun corsFilter(): CorsFilter? {
        if (!corsProperties.enabled) return null

        val config = CorsConfiguration().apply {
            allowCredentials = corsProperties.allowCredentials
            corsProperties.allowedOrigins.forEach { addAllowedOriginPattern(it) }
            corsProperties.allowedHeaders.forEach { addAllowedHeader(it) }
            corsProperties.allowedMethods.forEach { addAllowedMethod(it) }
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }
}