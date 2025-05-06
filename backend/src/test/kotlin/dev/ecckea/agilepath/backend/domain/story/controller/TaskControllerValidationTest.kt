package dev.ecckea.agilepath.backend.domain.story.controller

import dev.ecckea.agilepath.backend.config.TestAppConfig
import dev.ecckea.agilepath.backend.domain.story.dto.TaskRequest
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
class TaskControllerValidationTest : IntegrationTestBase() {

    private val validStoryId = UUID.fromString("11111111-0000-0000-0000-000000000001")
    private val validSprintColumnId = UUID.fromString("22222222-0000-0000-0000-000000000001")

    private fun postInvalidTask(request: TaskRequest): HttpStatusCode {
        return webTestClient
            .webPostWithAuth("/tasks", request)
            .exchange()
            .expectBody()
            .returnResult()
            .status
    }

    @Test
    fun `should reject blank title`() {
        val request = TaskRequest(
            storyId = validStoryId,
            sprintColumnId = validSprintColumnId,
            title = "   ",
            description = "Valid description",
            estimateTshirt = "MEDIUM",
            estimatePoints = "POINT_5",
            assigneeIds = listOf()
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidTask(request))
    }

    @Test
    fun `should reject too short title`() {
        val request = TaskRequest(
            storyId = validStoryId,
            sprintColumnId = validSprintColumnId,
            title = "ab", // Less than 3 characters
            description = "Valid description",
            estimateTshirt = "MEDIUM",
            estimatePoints = "POINT_5",
            assigneeIds = listOf()
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidTask(request))
    }

    @Test
    fun `should reject too long title`() {
        val request = TaskRequest(
            storyId = validStoryId,
            sprintColumnId = validSprintColumnId,
            title = "a".repeat(101), // 101 characters (exceeds 100 limit)
            description = "Valid description",
            estimateTshirt = "MEDIUM",
            estimatePoints = "POINT_5",
            assigneeIds = listOf()
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidTask(request))
    }

    @Test
    fun `should reject HTML in description`() {
        val request = TaskRequest(
            storyId = validStoryId,
            sprintColumnId = validSprintColumnId,
            title = "Valid Task Title",
            description = "<script>alert('xss')</script>",
            estimateTshirt = "MEDIUM",
            estimatePoints = "POINT_5",
            assigneeIds = listOf()
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidTask(request))
    }

    @Test
    fun `should reject too long description`() {
        val request = TaskRequest(
            storyId = validStoryId,
            sprintColumnId = validSprintColumnId,
            title = "Valid Task Title",
            description = "a".repeat(2001), // 2001 characters (exceeds 2000 limit)
            estimateTshirt = "MEDIUM",
            estimatePoints = "POINT_5",
            assigneeIds = listOf()
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidTask(request))
    }

    @Test
    fun `should reject invalid t-shirt estimate`() {
        val request = TaskRequest(
            storyId = validStoryId,
            sprintColumnId = validSprintColumnId,
            title = "Valid Task Title",
            description = "Valid description",
            estimateTshirt = "INVALID_SIZE", // Invalid enum value
            estimatePoints = "POINT_5",
            assigneeIds = listOf()
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidTask(request))
    }

    @Test
    fun `should reject invalid point estimate`() {
        val request = TaskRequest(
            storyId = validStoryId,
            sprintColumnId = validSprintColumnId,
            title = "Valid Task Title",
            description = "Valid description",
            estimateTshirt = "MEDIUM",
            estimatePoints = "POINT_100", // Invalid enum value
            assigneeIds = listOf()
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidTask(request))
    }

    @Test
    fun `should reject null story ID using JSON`() {
        val json = """
        {
            "sprintColumnId": "$validSprintColumnId",
            "title": "Valid Task Title",
            "description": "Valid description",
            "estimateTshirt": "MEDIUM",
            "estimatePoints": "POINT_5",
            "assigneeIds": []
        }
        """

        webTestClient
            .webPostWithAuth("/tasks", json)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `should reject null sprint column ID using JSON`() {
        val json = """
        {
            "storyId": "$validStoryId",
            "title": "Valid Task Title",
            "description": "Valid description",
            "estimateTshirt": "MEDIUM",
            "estimatePoints": "POINT_5",
            "assigneeIds": []
        }
        """

        webTestClient
            .webPostWithAuth("/tasks", json)
            .exchange()
            .expectStatus().isBadRequest
    }
}