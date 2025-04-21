package dev.ecckea.agilepath.backend.domain.user.controller

import dev.ecckea.agilepath.backend.config.TestAppConfig
import dev.ecckea.agilepath.backend.domain.user.dto.UserResponse
import dev.ecckea.agilepath.backend.support.IntegrationTestBase
import dev.ecckea.agilepath.backend.support.parseBody
import dev.ecckea.agilepath.backend.support.webGet
import dev.ecckea.agilepath.backend.support.webGetWithAuth
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import kotlin.test.Test

@SpringBootTest
@ActiveProfiles("test")
@Import(TestAppConfig::class)
class UserAuthControllerTest : IntegrationTestBase() {

    @Test
    fun `should return user profile when authenticated`() {
        val response = webTestClient.webGetWithAuth("/auth/profile")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .parseBody<UserResponse>()

        assert(response.id == "user-id")
        assert(response.email == "test@example.com")
        assert(response.fullName == "Test User")
        assert(response.githubUsername == "testuser")
        assert(response.githubProfileUrl == "https://github.com/testuser")
    }

    @Test
    fun `should return 401 when no token is provided`() {
        webTestClient.webGet("/auth/profile")
            .exchange()
            .expectStatus().isUnauthorized
    }

    @Test
    fun `should return 401 when token is invalid`() {
        webTestClient
            .webGetWithAuth("/auth/profile", token = "invalid-token")
            .exchange()
            .expectStatus().isUnauthorized
            .expectBody()
            .jsonPath("$.message").isEqualTo("Unauthorized – Invalid or missing token")
    }

}