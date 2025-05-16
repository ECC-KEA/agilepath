package dev.ecckea.agilepath.backend.domain.project.controller

import dev.ecckea.agilepath.backend.config.TestAppConfig
import dev.ecckea.agilepath.backend.domain.project.dto.ProjectRequest
import dev.ecckea.agilepath.backend.support.IntegrationTestBase
import dev.ecckea.agilepath.backend.support.webPostWithAuth
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestAppConfig::class)
class ProjectControllerValidationTest : IntegrationTestBase() {

    private fun postInvalidProject(request: ProjectRequest): HttpStatusCode {
        return webTestClient
            .webPostWithAuth("/projects", request)
            .exchange()
            .expectBody()
            .returnResult()
            .status
    }

    @Test
    fun `should reject blank name`() {
        val request = ProjectRequest(
            name = "   ",
            description = "Valid description",
            framework = "SCRUM"
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidProject(request))
    }

    @Test
    fun `should reject too short name`() {
        val request = ProjectRequest(
            name = "a",
            description = "Valid description",
            framework = "SCRUM"
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidProject(request))
    }

    @Test
    fun `should reject HTML in description`() {
        val request = ProjectRequest(
            name = "Valid name",
            description = "<b>malicious</b>",
            framework = "SCRUM"
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidProject(request))
    }

    @Test
    fun `should reject invalid framework`() {
        val request = ProjectRequest(
            name = "Valid name",
            description = "Valid description",
            framework = "KANBAN" // not part of enum
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidProject(request))
    }
}
