package dev.ecckea.agilepath.backend.domain.story.controller

import dev.ecckea.agilepath.backend.config.TestAppConfig
import dev.ecckea.agilepath.backend.domain.story.dto.TaskRequest
import dev.ecckea.agilepath.backend.domain.story.dto.TaskResponse
import dev.ecckea.agilepath.backend.domain.story.model.PointEstimate
import dev.ecckea.agilepath.backend.domain.story.model.TshirtEstimate
import dev.ecckea.agilepath.backend.support.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestAppConfig::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskControllerTest : IntegrationTestBase() {

    @Test
    fun `should create a new task`() {
        val request = buildTaskRequest()

        val response = webTestClient.webPostWithAuth("/tasks", request)
            .exchange()
            .expectStatus().isOk
            .parseBody<TaskResponse>()

        assertThat(response.title).isEqualTo(request.title)
        assertThat(response.description).isEqualTo(request.description)
        assertThat(response.estimateTshirt).isEqualTo(request.estimateTshirt)
        assertThat(response.estimatePoints).isEqualTo(request.estimatePoints)
        assertThat(response.storyId).isEqualTo(request.storyId)
        assertThat(response.sprintColumnId).isEqualTo(request.sprintColumnId)
        assertThat(response.id).isNotNull()
        assertThat(response.createdBy).isEqualTo("user-id")
    }

    @Test
    fun `should get an existing task`() {
        val created = createTask()

        val response = webTestClient.webGetWithAuth("/tasks/${created.id}")
            .exchange()
            .expectStatus().isOk
            .parseBody<TaskResponse>()

        assertThat(response.id).isEqualTo(created.id)
        assertThat(response.title).isEqualTo(created.title)
    }

    @Test
    fun `should update an existing task`() {
        val created = createTask()

        val updateRequest = buildTaskRequest(
            storyId = created.storyId,
            sprintColumnId = created.sprintColumnId,
            title = "Updated Task Title",
            description = "Updated description",
            estimateTshirt = TshirtEstimate.LARGE,
            estimatePoints = PointEstimate.POINT_13
        )

        val updated = webTestClient.webPutWithAuth("/tasks/${created.id}", updateRequest)
            .exchange()
            .expectStatus().isOk
            .parseBody<TaskResponse>()

        assertThat(updated.id).isEqualTo(created.id)
        assertThat(updated.title).isEqualTo(updateRequest.title)
        assertThat(updated.description).isEqualTo(updateRequest.description)
        assertThat(updated.estimateTshirt).isEqualTo(updateRequest.estimateTshirt)
        assertThat(updated.estimatePoints).isEqualTo(updateRequest.estimatePoints)
    }

    @Test
    fun `should delete an existing task`() {
        val created = createTask()

        webTestClient.webDeleteWithAuth("/tasks/${created.id}")
            .exchange()
            .expectStatus().isNoContent

        webTestClient.webGetWithAuth("/tasks/${created.id}")
            .exchange()
            .expectStatus().isNotFound
    }

    private fun createTask(): TaskResponse {
        val request = buildTaskRequest()
        return webTestClient.webPostWithAuth("/tasks", request)
            .exchange()
            .expectStatus().isOk
            .parseBody()
    }

    private fun buildTaskRequest(
        storyId: UUID = UUID.fromString("11111111-0000-0000-0000-000000000001"),
        sprintColumnId: UUID = UUID.fromString("c1111111-0000-0000-0000-000000000001"),
        title: String = "Task ${UUID.randomUUID()}",
        description: String = "Task description",
        estimateTshirt: TshirtEstimate = TshirtEstimate.SMALL,
        estimatePoints: PointEstimate = PointEstimate.POINT_3,
        assigneeIds: List<String> = listOf()
    ) = TaskRequest(
        storyId = storyId,
        sprintColumnId = sprintColumnId,
        title = title,
        description = description,
        estimateTshirt = estimateTshirt,
        estimatePoints = estimatePoints,
        assigneeIds = assigneeIds
    )

    @Test
    fun `should create task with assignees`() {
        // Create a task with assignees
        val request = buildTaskRequest(
            assigneeIds = listOf("user-id")
        )

        val created = webTestClient
            .webPostWithAuth("/tasks", request)
            .exchange()
            .expectStatus().isOk
            .parseBody<TaskResponse>()

        // Verify assignees were added
        assertThat(created.assignees).isNotNull()
        assertThat(created.assignees).hasSize(1)
        assertThat(created.assignees?.get(0)?.id).isEqualTo("user-id")
    }

    @Test
    fun `should update task by adding assignees`() {
        // First create a task without assignees
        val initial = createTask()

        // Verify it has no assignees
        assertThat(initial.assignees).isEmpty()

        // Update task to add assignees
        val updateRequest = buildTaskRequest(
            storyId = initial.storyId,
            sprintColumnId = initial.sprintColumnId,
            title = initial.title,
            assigneeIds = listOf("user-id")
        )

        val updated = webTestClient
            .webPutWithAuth("/tasks/${initial.id}", updateRequest)
            .exchange()
            .expectStatus().isOk
            .parseBody<TaskResponse>()

        // Verify assignee was added
        assertThat(updated.assignees).hasSize(1)
        assertThat(updated.assignees?.get(0)?.id).isEqualTo("user-id")
    }

    @Test
    fun `should update task by removing assignees`() {
        // First create a task with assignees
        val request = buildTaskRequest(
            assigneeIds = listOf("user-id")
        )

        val taskWithAssignee = webTestClient
            .webPostWithAuth("/tasks", request)
            .exchange()
            .expectStatus().isOk
            .parseBody<TaskResponse>()

        // Verify it has an assignee
        assertThat(taskWithAssignee.assignees).hasSize(1)

        // Update task to remove assignee
        val updateRequest = buildTaskRequest(
            storyId = taskWithAssignee.storyId,
            sprintColumnId = taskWithAssignee.sprintColumnId,
            title = taskWithAssignee.title,
            assigneeIds = listOf() // Empty list = no assignees
        )

        val updated = webTestClient
            .webPutWithAuth("/tasks/${taskWithAssignee.id}", updateRequest)
            .exchange()
            .expectStatus().isOk
            .parseBody<TaskResponse>()

        // Verify assignee was removed
        assertThat(updated.assignees).isEmpty()
    }

    @Test
    fun `should update task by changing assignees`() {
        // For this test, we'll need another user ID
        val firstUserId = "user-id"
        val secondUserId = "dummy-user-1" // Using an existing user from your seed data

        // First create a task with one assignee
        val request = buildTaskRequest(
            assigneeIds = listOf(firstUserId)
        )

        val initial = webTestClient
            .webPostWithAuth("/tasks", request)
            .exchange()
            .expectStatus().isOk
            .parseBody<TaskResponse>()

        // Verify it has the first assignee
        assertThat(initial.assignees).hasSize(1)
        assertThat(initial.assignees?.get(0)?.id).isEqualTo(firstUserId)

        // Update task to change assignees
        val updateRequest = buildTaskRequest(
            storyId = initial.storyId,
            sprintColumnId = initial.sprintColumnId,
            title = initial.title,
            assigneeIds = listOf(secondUserId)
        )

        val updated = webTestClient
            .webPutWithAuth("/tasks/${initial.id}", updateRequest)
            .exchange()
            .expectStatus().isOk
            .parseBody<TaskResponse>()

        // Verify assignee was changed
        assertThat(updated.assignees).hasSize(1)
        assertThat(updated.assignees?.get(0)?.id).isEqualTo(secondUserId)
    }
}
