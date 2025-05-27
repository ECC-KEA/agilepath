package dev.ecckea.agilepath.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cors")
data class CorsProperties(
    var enabled: Boolean = true,
    var allowedOrigins: List<String> = listOf(),
    var allowedMethods: List<String> = listOf("GET", "POST", "PATCH", "PUT"),
    var allowedHeaders: List<String> = listOf("*"),
    var allowCredentials: Boolean = true
)