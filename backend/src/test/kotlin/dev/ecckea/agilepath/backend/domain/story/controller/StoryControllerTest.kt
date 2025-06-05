package dev.ecckea.agilepath.backend.domain.story.controller

import dev.ecckea.agilepath.backend.config.TestAppConfig
import dev.ecckea.agilepath.backend.domain.story.dto.StoryRequest
import dev.ecckea.agilepath.backend.domain.story.dto.StoryResponse
import dev.ecckea.agilepath.backend.support.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestAppConfig::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StoryControllerTest : IntegrationTestBase() {

    companion object {
        val existingProjectId = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val existingStoryId = UUID.fromString("11111111-0000-0000-0000-000000000001")
        val nonExistingStoryId = UUID.fromString("99999999-9999-9999-9999-999999999999")
    }

    private fun createStoryRequest(
        projectId: UUID = existingProjectId,
        title: String = "Test Story ${UUID.randomUUID()}",
        description: String? = "Description for the story",
        acceptanceCriteria: String? = "Acceptance criteria for the story",
        status: String = "TODO",
        priority: Int = 1
    ) = StoryRequest(
        projectId = projectId,
        title = title,
        description = description,
        acceptanceCriteria = acceptanceCriteria,
        status = status,
        priority = priority
    )

    @Test
    fun `should create a new story`() {
        val request = createStoryRequest()

        val created = webTestClient
            .webPostWithAuth("/stories", request)
            .exchange()
            .expectStatus().isOk
            .parseBody<StoryResponse>()

        assertNotNull(created.id)
        assertEquals(request.title, created.title)
        assertEquals(request.description, created.description)
        assertEquals(request.status, created.status)
        assertEquals(request.priority, created.priority)
        assertEquals(request.projectId, created.projectId)
        assertNotNull(created.createdAt)
        assertEquals("user-id", created.createdBy)
    }

    @Test
    fun `should fetch an existing story`() {
        val fetched = webTestClient
            .webGetWithAuth("/stories/$existingStoryId")
            .exchange()
            .expectStatus().isOk
            .parseBody<StoryResponse>()

        assertEquals(existingStoryId, fetched.id)
        assertEquals(existingProjectId, fetched.projectId)
        assertNotNull(fetched.title)
        assertNotNull(fetched.createdAt)
    }

    @Test
    fun `should update an existing story`() {
        val updateRequest = createStoryRequest(
            title = "Updated Story Title",
            description = "Updated Description",
            acceptanceCriteria = "Updated Acceptance Criteria",
            status = "IN_PROGRESS",
            priority = 2
        )

        val updated = webTestClient
            .webPutWithAuth("/stories/$existingStoryId", updateRequest)
            .exchange()
            .expectStatus().isOk
            .parseBody<StoryResponse>()

        assertEquals(existingStoryId, updated.id)
        assertEquals("Updated Story Title", updated.title)
        assertEquals("Updated Description", updated.description)
        assertEquals("IN_PROGRESS", updated.status)
        assertEquals(2, updated.priority)
    }

    @Test
    fun `should delete an existing story`() {
        // First create a story to delete
        val created = webTestClient
            .webPostWithAuth("/stories", createStoryRequest())
            .exchange()
            .expectStatus().isOk
            .parseBody<StoryResponse>()

        // Then delete it
        webTestClient
            .webDeleteWithAuth("/stories/${created.id}")
            .exchange()
            .expectStatus().isOk

        // Then verify it no longer exists
        webTestClient
            .webGetWithAuth("/stories/${created.id}")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should return 404 when fetching non-existing story`() {
        webTestClient
            .webGetWithAuth("/stories/$nonExistingStoryId")
            .exchange()
            .expectStatus().isNotFound
    }
}
