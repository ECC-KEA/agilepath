package dev.ecckea.agilepath.backend

import dev.ecckea.agilepath.backend.config.loadEnv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BackendApplication

fun main(args: Array<String>) {
    if (System.getenv("SPRING_PROFILES_ACTIVE") == "dev") {
        dev.ecckea.agilepath.backend.config.loadEnv()
    }
    runApplication<BackendApplication>(*args)
}
