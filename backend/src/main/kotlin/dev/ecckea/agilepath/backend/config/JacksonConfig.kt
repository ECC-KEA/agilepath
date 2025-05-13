package dev.ecckea.agilepath.backend.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

/**
 * Configuration class for customizing Jackson ObjectMapper instances.
 *
 * This class provides beans for the default ObjectMapper and a specialized
 * ObjectMapper for Redis cache serialization.
 */
@Configuration
class JacksonConfig {

    /**
     * Configures the primary ObjectMapper bean.
     *
     * This ObjectMapper is customized with the following features:
     * - Registers the JavaTimeModule for handling Java 8 date and time types.
     * - Registers the KotlinModule for better Kotlin support.
     * - Automatically registers additional modules found on the classpath.
     * - Disables writing dates as timestamps.
     *
     * @return a configured instance of ObjectMapper.
     */
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerModule(JavaTimeModule())
            .registerModule(KotlinModule.Builder().build())
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }
}