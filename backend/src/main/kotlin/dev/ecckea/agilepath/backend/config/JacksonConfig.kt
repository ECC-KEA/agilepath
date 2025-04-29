package dev.ecckea.agilepath.backend.config

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
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

    companion object {
        /**
         * The base package name used for polymorphic type validation.
         */
        private const val APP_PACKAGE = "dev.ecckea.agilepath"
    }

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

    /**
     * Configures a specialized ObjectMapper bean for Redis cache serialization.
     *
     * This ObjectMapper is customized with the following features:
     * - Registers the JavaTimeModule for handling Java 8 date and time types.
     * - Registers the KotlinModule for better Kotlin support.
     * - Automatically registers additional modules found on the classpath.
     * - Disables writing dates as timestamps.
     * - Activates default typing for non-final types using a polymorphic type validator.
     * - Adds mix-ins for common collection types to customize their serialization.
     *
     * @return a configured instance of ObjectMapper for Redis cache serialization.
     */
    @Bean(name = ["redisCacheObjectMapper"])
    fun redisCacheObjectMapper(): ObjectMapper {
        val ptv = BasicPolymorphicTypeValidator.builder()
            .allowIfBaseType(APP_PACKAGE)
            .allowIfSubType(APP_PACKAGE)
            .allowIfSubType("java.time")
            .build()

        val objectMapper = ObjectMapper()
            .registerModule(JavaTimeModule())
            .registerModule(KotlinModule.Builder().build())
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

        objectMapper.activateDefaultTyping(
            ptv,
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY
        )

        objectMapper.addMixIn(List::class.java, JacksonCollectionMixin::class.java)
        objectMapper.addMixIn(Map::class.java, JacksonCollectionMixin::class.java)
        objectMapper.addMixIn(Set::class.java, JacksonCollectionMixin::class.java)
        objectMapper.addMixIn(ArrayList::class.java, JacksonCollectionMixin::class.java)
        objectMapper.addMixIn(HashMap::class.java, JacksonCollectionMixin::class.java)
        objectMapper.addMixIn(LinkedHashMap::class.java, JacksonCollectionMixin::class.java)

        return objectMapper
    }

    /**
     * A mix-in interface to customize the serialization of collection types.
     *
     * This mix-in disables type information for the annotated collection types.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    interface JacksonCollectionMixin
}