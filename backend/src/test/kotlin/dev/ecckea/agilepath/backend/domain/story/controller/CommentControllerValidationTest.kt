package dev.ecckea.agilepath.backend.domain.story.controller

import dev.ecckea.agilepath.backend.config.TestAppConfig
import dev.ecckea.agilepath.backend.domain.story.dto.CommentRequest
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
class CommentControllerValidationTest : IntegrationTestBase() {

    private val validStoryId = UUID.fromString("11111111-0000-0000-0000-000000000001")
    private val validTaskId = UUID.fromString("22222222-0000-0000-0000-000000000001")

    private fun postInvalidComment(request: CommentRequest): HttpStatusCode {
        return webTestClient
            .webPostWithAuth("/comments", request)
            .exchange()
            .expectBody()
            .returnResult()
            .status
    }

    @Test
    fun `should reject blank content`() {
        val request = CommentRequest(
            content = "   ",
            storyId = validStoryId,
            taskId = null
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidComment(request))
    }

    @Test
    fun `should reject too long content`() {
        val request = CommentRequest(
            content = "a".repeat(2001), // 2001 characters (exceeds 2000 limit)
            storyId = validStoryId,
            taskId = null
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidComment(request))
    }

    @Test
    fun `should reject HTML in content`() {
        val request = CommentRequest(
            content = "<script>alert('xss')</script>",
            storyId = validStoryId,
            taskId = null
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidComment(request))
    }

    @Test
    fun `should reject both task and story IDs provided`() {
        val request = CommentRequest(
            content = "Valid comment content",
            storyId = validStoryId,
            taskId = validTaskId
        )
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, postInvalidComment(request))
    }

    @Test
    fun `should reject neither task nor story ID provided`() {
        val request = CommentRequest(
            content = "Valid comment content",
            storyId = null,
            taskId = null
        )
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, postInvalidComment(request))
    }
}