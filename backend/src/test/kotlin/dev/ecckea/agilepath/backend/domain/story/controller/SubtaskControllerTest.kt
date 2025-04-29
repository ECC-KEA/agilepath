package dev.ecckea.agilepath.backend.domain.story.controller

import dev.ecckea.agilepath.backend.config.TestAppConfig
import dev.ecckea.agilepath.backend.domain.story.dto.SubtaskRequest
import dev.ecckea.agilepath.backend.domain.story.dto.SubtaskResponse
import dev.ecckea.agilepath.backend.support.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestAppConfig::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SubtaskControllerTest : IntegrationTestBase() {

    companion object {
        val existingTaskId = UUID.fromString("22222222-0000-0000-0000-000000000001")
        val existingSubtaskId = UUID.fromString("33333333-0000-0000-0000-000000000001")
        val nonExistingSubtaskId = UUID.fromString("99999999-9999-9999-9999-999999999999")
    }

    private fun createSubtaskRequest(
        taskId: UUID = existingTaskId,
        title: String = "Test Subtask ${UUID.randomUUID()}",
        description: String? = "Description for the subtask",
        isDone: Boolean = false
    ) = SubtaskRequest(
        taskId = taskId,
        title = title,
        description = description,
        isDone = isDone
    )

    private fun createSubtask(): SubtaskResponse {
        return webTestClient
            .webPostWithAuth("/subtasks", createSubtaskRequest())
            .exchange()
            .expectStatus().isOk
            .parseBody()
    }

    @Test
    fun `should create a new subtask`() {
        val request = createSubtaskRequest()

        val created = webTestClient
            .webPostWithAuth("/subtasks", request)
            .exchange()
            .expectStatus().isOk
            .parseBody<SubtaskResponse>()

        assertNotNull(created.id)
        assertEquals(request.taskId, created.taskId)
        assertEquals(request.title, created.title)
        assertEquals(request.description, created.description)
        assertEquals(request.isDone, created.isDone)
        assertNotNull(created.createdAt)
        assertEquals("user-id", created.createdBy)
    }

    @Test
    fun `should fetch an existing subtask`() {
        val fetched = webTestClient
            .webGetWithAuth("/subtasks/$existingSubtaskId")
            .exchange()
            .expectStatus().isOk
            .parseBody<SubtaskResponse>()

        assertEquals(existingSubtaskId, fetched.id)
        assertEquals(existingTaskId, fetched.taskId)
        assertNotNull(fetched.title)
        assertNotNull(fetched.createdAt)
    }

    @Test
    fun `should fetch subtasks by task ID`() {
        val subtasks = webTestClient
            .webGetWithAuth("/subtasks/task/$existingTaskId")
            .exchange()
            .expectStatus().isOk
            .parseListBody<SubtaskResponse>()

        assertNotNull(subtasks)
        assertTrue(subtasks.isNotEmpty())
        assertEquals(existingTaskId, subtasks[0].taskId)
    }

    @Test
    fun `should update an existing subtask`() {
        val updateRequest = createSubtaskRequest(
            title = "Updated Subtask Title",
            description = "Updated Description",
            isDone = true
        )

        val updated = webTestClient
            .webPutWithAuth("/subtasks/$existingSubtaskId", updateRequest)
            .exchange()
            .expectStatus().isOk
            .parseBody<SubtaskResponse>()

        assertEquals(existingSubtaskId, updated.id)
        assertEquals("Updated Subtask Title", updated.title)
        assertEquals("Updated Description", updated.description)
        assertTrue(updated.isDone)
    }

    @Test
    fun `should toggle subtask status`() {
        // First get the current status
        val current = webTestClient
            .webGetWithAuth("/subtasks/$existingSubtaskId")
            .exchange()
            .expectStatus().isOk
            .parseBody<SubtaskResponse>()

        // Then toggle it
        val toggled = webTestClient
            .webPatchWithAuth("/subtasks/$existingSubtaskId/toggle")
            .exchange()
            .expectStatus().isOk
            .parseBody<SubtaskResponse>()

        assertEquals(existingSubtaskId, toggled.id)
        assertEquals(!current.isDone, toggled.isDone)
    }

    @Test
    fun `should delete an existing subtask`() {
        // First create a subtask to delete
        val created = createSubtask()

        // Then delete it
        webTestClient
            .webDeleteWithAuth("/subtasks/${created.id}")
            .exchange()
            .expectStatus().isNoContent

        // Then verify it no longer exists
        webTestClient
            .webGetWithAuth("/subtasks/${created.id}")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should return 404 when fetching non-existing subtask`() {
        webTestClient
            .webGetWithAuth("/subtasks/$nonExistingSubtaskId")
            .exchange()
            .expectStatus().isNotFound
    }
}