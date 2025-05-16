package dev.ecckea.agilepath.backend.domain.story.controller

import dev.ecckea.agilepath.backend.config.TestAppConfig
import dev.ecckea.agilepath.backend.domain.story.dto.StoryRequest
import dev.ecckea.agilepath.backend.support.IntegrationTestBase
import dev.ecckea.agilepath.backend.support.webPostWithAuth
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.test.context.ActiveProfiles
import java.util.*
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestAppConfig::class)
class StoryControllerValidationTest : IntegrationTestBase() {

    private val validProjectId = UUID.fromString("11111111-0000-0000-0000-000000000001")

    private fun postInvalidStory(request: StoryRequest): HttpStatusCode {
        return webTestClient
            .webPostWithAuth("/stories", request)
            .exchange()
            .expectBody()
            .returnResult()
            .status
    }

    @Test
    fun `should reject blank title`() {
        val request = StoryRequest(
            projectId = validProjectId,
            title = "   ",
            description = "Valid description",
            status = "TO_DO",
            priority = 0
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidStory(request))
    }

    @Test
    fun `should reject too short title`() {
        val request = StoryRequest(
            projectId = validProjectId,
            title = "ab", // Less than 3 characters
            description = "Valid description",
            status = "TO_DO",
            priority = 0
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidStory(request))
    }

    @Test
    fun `should reject too long title`() {
        val request = StoryRequest(
            projectId = validProjectId,
            title = "a".repeat(101), // 101 characters (exceeds 100 limit)
            description = "Valid description",
            status = "TO_DO",
            priority = 0
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidStory(request))
    }

    @Test
    fun `should reject HTML in description`() {
        val request = StoryRequest(
            projectId = validProjectId,
            title = "Valid Story Title",
            description = "<script>alert('xss')</script>",
            status = "TO_DO",
            priority = 0
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidStory(request))
    }

    @Test
    fun `should reject too long description`() {
        val request = StoryRequest(
            projectId = validProjectId,
            title = "Valid Story Title",
            description = "a".repeat(2001), // 2001 characters (exceeds 2000 limit)
            status = "TO_DO",
            priority = 0
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidStory(request))
    }

    @Test
    fun `should reject blank status`() {
        val request = StoryRequest(
            projectId = validProjectId,
            title = "Valid Story Title",
            description = "Valid description",
            status = "   ",
            priority = 0
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidStory(request))
    }

    @Test
    fun `should reject negative priority`() {
        val request = StoryRequest(
            projectId = validProjectId,
            title = "Valid Story Title",
            description = "Valid description",
            status = "TO_DO",
            priority = -1
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidStory(request))
    }

    @Test
    fun `should reject null project ID using JSON`() {
        val json = """
        {
            "title": "Valid Story Title",
            "description": "Valid description",
            "status": "TO_DO",
            "priority": 0
        }
        """

        webTestClient
            .webPostWithAuth("/stories", json)
            .exchange()
            .expectStatus().isBadRequest
    }
}