package dev.ecckea.agilepath.backend.config

import io.github.cdimascio.dotenv.dotenv

/**
 * Loads environment variables from a local `.env` file
 * and registers them as system properties.
 *
 * This is typically used during development to provide
 * database credentials, API keys, and other environment-specific
 * values without hardcoding them in configuration files.
 *
 * The loaded properties can be accessed using `${VAR_NAME}` in
 * `application.yml`.
 *
 * Example:
 *  DB_URL=jdbc:postgresql://localhost:5432/dev
 *  API_KEY=supersecret
 */
fun loadEnv() {
    val dotenv = dotenv()
    dotenv.entries().forEach { entry ->
        System.setProperty(entry.key, entry.value)
    }
}

/**
 * Loads environment variables from a `.env.test` file
 * and registers them as system properties for use during tests.
 *
 * This function is intended for use in integration and system tests
 * where test-specific values (such as a test database URL) are required.
 *
 * If the `.env.test` file is not found, it will silently continue
 * without throwing an error (`ignoreIfMissing = true`).
 *
 * Should be called in a @BeforeAll block or a base test class.
 *
 * Example:
 *  JDBC_DATABASE_URL=jdbc:postgresql://localhost:5432/agilepath_test
 *  TEST_API_KEY=fake-key
 */
fun loadTestEnv() {
    val dotenv = dotenv {
        filename = ".env.test"
        ignoreIfMissing = true
    }
    dotenv.entries().forEach { entry ->
        System.setProperty(entry.key, entry.value)
    }
}