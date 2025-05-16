package dev.ecckea.agilepath.backend.domain.story.controller

import dev.ecckea.agilepath.backend.config.TestAppConfig
import dev.ecckea.agilepath.backend.domain.story.dto.CommentRequest
import dev.ecckea.agilepath.backend.domain.story.dto.CommentResponse
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
class CommentControllerTest : IntegrationTestBase() {

    companion object {
        val existingStoryId = UUID.fromString("11111111-0000-0000-0000-000000000001")
        val existingTaskId = UUID.fromString("22222222-0000-0000-0000-000000000001")
        val existingCommentId = UUID.fromString("44444444-0000-0000-0000-000000000001")
        val nonExistingCommentId = UUID.fromString("99999999-9999-9999-9999-999999999999")
    }

    private fun createStoryCommentRequest(
        storyId: UUID = existingStoryId,
        content: String = "Test comment for story ${UUID.randomUUID()}"
    ) = CommentRequest(
        content = content,
        storyId = storyId,
        taskId = null
    )

    private fun createTaskCommentRequest(
        taskId: UUID = existingTaskId,
        content: String = "Test comment for task ${UUID.randomUUID()}"
    ) = CommentRequest(
        content = content,
        storyId = null,
        taskId = taskId
    )

    private fun createStoryComment(): CommentResponse {
        return webTestClient
            .webPostWithAuth("/comments", createStoryCommentRequest())
            .exchange()
            .expectStatus().isOk
            .parseBody()
    }

    private fun createTaskComment(): CommentResponse {
        return webTestClient
            .webPostWithAuth("/comments", createTaskCommentRequest())
            .exchange()
            .expectStatus().isOk
            .parseBody()
    }

    @Test
    fun `should create a new comment for story`() {
        val request = createStoryCommentRequest()

        val created = webTestClient
            .webPostWithAuth("/comments", request)
            .exchange()
            .expectStatus().isOk
            .parseBody<CommentResponse>()

        assertNotNull(created.id)
        assertEquals(request.content, created.content)
        assertEquals(request.storyId, created.storyId)
        assertEquals(null, created.taskId)
        assertNotNull(created.createdAt)
        assertEquals("user-id", created.createdBy)
    }

    @Test
    fun `should create a new comment for task`() {
        val request = createTaskCommentRequest()

        val created = webTestClient
            .webPostWithAuth("/comments", request)
            .exchange()
            .expectStatus().isOk
            .parseBody<CommentResponse>()

        assertNotNull(created.id)
        assertEquals(request.content, created.content)
        assertEquals(null, created.storyId)
        assertEquals(request.taskId, created.taskId)
        assertNotNull(created.createdAt)
        assertEquals("user-id", created.createdBy)
    }

    @Test
    fun `should reject comment with both story and task id`() {
        val request = CommentRequest(
            content = "Invalid comment",
            storyId = existingStoryId,
            taskId = existingTaskId
        )

        webTestClient
            .webPostWithAuth("/comments", request)
            .exchange()
            .expectStatus().isEqualTo(422)
    }

    @Test
    fun `should reject comment with neither story nor task id`() {
        val request = CommentRequest(
            content = "Invalid comment",
            storyId = null,
            taskId = null
        )

        webTestClient
            .webPostWithAuth("/comments", request)
            .exchange()
            .expectStatus().isEqualTo(422)
    }

    @Test
    fun `should fetch an existing comment`() {
        val fetched = webTestClient
            .webGetWithAuth("/comments/$existingCommentId")
            .exchange()
            .expectStatus().isOk
            .parseBody<CommentResponse>()

        assertEquals(existingCommentId, fetched.id)
        assertNotNull(fetched.content)
        assertTrue { fetched.storyId != null || fetched.taskId != null }
        assertNotNull(fetched.createdAt)
    }

    @Test
    fun `should fetch comments by story ID`() {
        val comments = webTestClient
            .webGetWithAuth("/comments/story/$existingStoryId")
            .exchange()
            .expectStatus().isOk
            .parseListBody<CommentResponse>()

        assertNotNull(comments)
        assertTrue(comments.isNotEmpty())
        comments.forEach { comment ->
            assertEquals(existingStoryId, comment.storyId)
            assertEquals(null, comment.taskId)
        }
    }

    @Test
    fun `should fetch comments by task ID`() {
        val comments = webTestClient
            .webGetWithAuth("/comments/task/$existingTaskId")
            .exchange()
            .expectStatus().isOk
            .parseListBody<CommentResponse>()

        assertNotNull(comments)
        assertTrue(comments.isNotEmpty())
        comments.forEach { comment ->
            assertEquals(null, comment.storyId)
            assertEquals(existingTaskId, comment.taskId)
        }
    }

    @Test
    fun `should update an existing comment`() {
        // First create a comment to update
        val created = createStoryComment()

        val updateRequest = CommentRequest(
            content = "Updated comment content",
            storyId = created.storyId,
            taskId = null
        )

        val updated = webTestClient
            .webPutWithAuth("/comments/${created.id}", updateRequest)
            .exchange()
            .expectStatus().isOk
            .parseBody<CommentResponse>()

        assertEquals(created.id, updated.id)
        assertEquals("Updated comment content", updated.content)
        assertEquals(created.storyId, updated.storyId)
        assertEquals(created.taskId, updated.taskId)
    }

    @Test
    fun `should reject update that changes comment target`() {
        // First create a comment to update
        val created = createStoryComment()

        // Try to change from story to task
        val updateRequest = CommentRequest(
            content = "Updated comment content",
            storyId = null,
            taskId = existingTaskId
        )

        webTestClient
            .webPutWithAuth("/comments/${created.id}", updateRequest)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `should delete an existing comment`() {
        // First create a comment to delete
        val created = createStoryComment()

        // Then delete it
        webTestClient
            .webDeleteWithAuth("/comments/${created.id}")
            .exchange()
            .expectStatus().isNoContent

        // Then verify it no longer exists
        webTestClient
            .webGetWithAuth("/comments/${created.id}")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should return 404 when fetching non-existing comment`() {
        webTestClient
            .webGetWithAuth("/comments/$nonExistingCommentId")
            .exchange()
            .expectStatus().isNotFound
    }
}