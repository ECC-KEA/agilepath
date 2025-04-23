package dev.ecckea.agilepath.backend.config

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration class for customizing the Jackson `ObjectMapper` bean.
 *
 * This class defines a Spring-managed bean for `ObjectMapper` with the following configurations:
 * - Registers the `JavaTimeModule` for handling Java 8 date and time types.
 * - Registers the `KotlinModule` for better Kotlin support.
 * - Automatically registers additional modules found on the classpath.
 * - Activates default typing for polymorphic type handling with a custom `BasicPolymorphicTypeValidator`.
 * - Disables the serialization of dates as timestamps.
 */
@Configuration
class JacksonConfig {

    /**
     * Creates and configures a custom `ObjectMapper` bean.
     *
     * This method sets up the `ObjectMapper` with:
     * - A `BasicPolymorphicTypeValidator` to allow polymorphic type handling for specific base and subtypes.
     * - Modules for Java time and Kotlin support.
     * - Default typing for non-final types, using property-based inclusion.
     * - Disabling of date serialization as timestamps.
     *
     * @return a configured instance of `ObjectMapper`.
     */
    @Bean
    fun objectMapper(): ObjectMapper {
        val ptv = BasicPolymorphicTypeValidator.builder()
            .allowIfBaseType(Any::class.java)
            .allowIfSubType("dev.ecckea.agilepath")
            .allowIfSubType("java.util")
            .allowIfSubType("java.time")
            .build()

        return ObjectMapper()
            .registerModule(JavaTimeModule())
            .registerModule(KotlinModule.Builder().build())
            .findAndRegisterModules()
            .activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }
}
