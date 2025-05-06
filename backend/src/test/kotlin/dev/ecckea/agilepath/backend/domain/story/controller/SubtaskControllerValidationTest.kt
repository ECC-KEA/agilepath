package dev.ecckea.agilepath.backend.domain.story.controller

import dev.ecckea.agilepath.backend.config.TestAppConfig
import dev.ecckea.agilepath.backend.domain.story.dto.SubtaskRequest
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
class SubtaskControllerValidationTest : IntegrationTestBase() {

    private val validTaskId = UUID.fromString("22222222-0000-0000-0000-000000000001")

    private fun postInvalidSubtask(request: SubtaskRequest): HttpStatusCode {
        return webTestClient
            .webPostWithAuth("/subtasks", request)
            .exchange()
            .expectBody()
            .returnResult()
            .status
    }

    @Test
    fun `should reject blank title`() {
        val request = SubtaskRequest(
            taskId = validTaskId,
            title = "   ",
            description = "Valid description",
            isDone = false
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidSubtask(request))
    }

    @Test
    fun `should reject too short title`() {
        val request = SubtaskRequest(
            taskId = validTaskId,
            title = "ab", // Less than 3 characters
            description = "Valid description",
            isDone = false
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidSubtask(request))
    }

    @Test
    fun `should reject too long title`() {
        val request = SubtaskRequest(
            taskId = validTaskId,
            title = "a".repeat(101), // 101 characters (exceeds 100 limit)
            description = "Valid description",
            isDone = false
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidSubtask(request))
    }

    @Test
    fun `should reject HTML in description`() {
        val request = SubtaskRequest(
            taskId = validTaskId,
            title = "Valid Subtask Title",
            description = "<script>alert('xss')</script>",
            isDone = false
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidSubtask(request))
    }

    @Test
    fun `should reject too long description`() {
        val request = SubtaskRequest(
            taskId = validTaskId,
            title = "Valid Subtask Title",
            description = "a".repeat(2001), // 2001 characters (exceeds 2000 limit)
            isDone = false
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidSubtask(request))
    }

    @Test
    fun `should reject null task ID using JSON`() {
        val json = """
        {
            "title": "Valid Subtask Title",
            "description": "Valid description",
            "isDone": false
        }
        """

        webTestClient
            .webPostWithAuth("/subtasks", json)
            .exchange()
            .expectStatus().isBadRequest
    }
}