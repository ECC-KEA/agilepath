package dev.ecckea.agilepath.backend

import dev.ecckea.agilepath.backend.config.CorsProperties
import dev.ecckea.agilepath.backend.config.loadEnv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(CorsProperties::class)
class BackendApplication

fun main(args: Array<String>) {
    if (System.getenv("SPRING_PROFILES_ACTIVE") == "dev") {
        loadEnv()
    }
    runApplication<BackendApplication>(*args)
}
