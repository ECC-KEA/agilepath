package dev.ecckea.agilepath.backend.domain.column.controller

import dev.ecckea.agilepath.backend.config.TestAppConfig
import dev.ecckea.agilepath.backend.domain.column.dto.SprintColumnRequest
import dev.ecckea.agilepath.backend.domain.column.dto.SprintColumnResponse
import dev.ecckea.agilepath.backend.domain.column.model.SprintColumnStatus
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
class SprintColumnControllerTest : IntegrationTestBase() {

    private val testSprintId: UUID = UUID.fromString("11111111-0000-0000-0000-000000000001")

    // Helper method to create a column request
    private fun createColumnRequest(
        sprintId: UUID = testSprintId,
        name: String = "Column-${UUID.randomUUID()}",
        status: SprintColumnStatus = SprintColumnStatus.TODO,
        columnIndex: Int = 0
    ) = SprintColumnRequest(
        sprintId = sprintId,
        name = name,
        columnStatus = status,
        columnIndex = columnIndex
    )

    // Helper method to create a column
    private fun createColumn(request: SprintColumnRequest = createColumnRequest()): SprintColumnResponse {
        return webTestClient
            .webPostWithAuth("/sprint-columns", request)
            .exchange()
            .expectStatus().isOk
            .parseBody()
    }

    @Test
    fun `should create a sprint column`() {
        val request = createColumnRequest(
            name = "Backlog",
            status = SprintColumnStatus.TODO,
            columnIndex = 0
        )

        val created = webTestClient
            .webPostWithAuth("/sprint-columns", request)
            .exchange()
            .expectStatus().isOk
            .parseBody<SprintColumnResponse>()

        assertNotNull(created.id)
        assertEquals(request.sprintId, created.sprintId)
        assertEquals(request.name, created.name)
        assertEquals(request.columnStatus.toString(), created.status)
        assertEquals(request.columnIndex, created.columnIndex)
    }

    @Test
    fun `should create columns with different statuses`() {
        val todoColumn = createColumn(
            createColumnRequest(
                name = "To Do",
                status = SprintColumnStatus.TODO,
                columnIndex = 0
            )
        )
        assertEquals(SprintColumnStatus.TODO.toString(), todoColumn.status)

        val inProgressColumn = createColumn(
            createColumnRequest(
                name = "In Progress",
                status = SprintColumnStatus.IN_PROGRESS,
                columnIndex = 1
            )
        )
        assertEquals(SprintColumnStatus.IN_PROGRESS.toString(), inProgressColumn.status)

        val doneColumn = createColumn(
            createColumnRequest(
                name = "Done",
                status = SprintColumnStatus.DONE,
                columnIndex = 2
            )
        )
        assertEquals(SprintColumnStatus.DONE.toString(), doneColumn.status)
    }

    @Test
    fun `should fetch a column by id`() {
        val created = createColumn()

        val fetched = webTestClient
            .webGetWithAuth("/sprint-columns/${created.id}")
            .exchange()
            .expectStatus().isOk
            .parseBody<SprintColumnResponse>()

        assertEquals(created.id, fetched.id)
        assertEquals(created.sprintId, fetched.sprintId)
        assertEquals(created.name, fetched.name)
        assertEquals(created.status, fetched.status)
        assertEquals(created.columnIndex, fetched.columnIndex)
    }

    @Test
    fun `should fetch all columns for a sprint`() {
        // Create a few columns for our test sprint
        val column1 = createColumn(
            createColumnRequest(
                sprintId = testSprintId,
                name = "Backlog",
                status = SprintColumnStatus.TODO,
                columnIndex = 0
            )
        )
        val column2 = createColumn(
            createColumnRequest(
                sprintId = testSprintId,
                name = "In Progress",
                status = SprintColumnStatus.IN_PROGRESS,
                columnIndex = 1
            )
        )

        val columns = webTestClient
            .webGetWithAuth("/sprints/${testSprintId}/sprint-columns")
            .exchange()
            .expectStatus().isOk
            .parseListBody<SprintColumnResponse>()

        assertTrue(columns.size >= 2)
        assertTrue(columns.any { it.id == column1.id })
        assertTrue(columns.any { it.id == column2.id })
    }

    @Test
    fun `should update a column`() {
        val created = createColumn()

        val updatedRequest = SprintColumnRequest(
            sprintId = created.sprintId,
            name = "Updated Column Name",
            columnStatus = SprintColumnStatus.IN_PROGRESS,
            columnIndex = 3
        )

        val updated = webTestClient
            .webPutWithAuth("/sprint-columns/${created.id}", updatedRequest)
            .exchange()
            .expectStatus().isOk
            .parseBody<SprintColumnResponse>()

        assertEquals(created.id, updated.id)
        assertEquals("Updated Column Name", updated.name)
        assertEquals(SprintColumnStatus.IN_PROGRESS.toString(), updated.status)
        assertEquals(3, updated.columnIndex)
    }

    @Test
    fun `should not allow changing sprint ID during update`() {
        val created = createColumn()
        val differentSprintId = UUID.randomUUID()

        val invalidUpdateRequest = SprintColumnRequest(
            sprintId = differentSprintId, // Different sprint ID
            name = created.name,
            columnStatus = SprintColumnStatus.valueOf(created.status),
            columnIndex = created.columnIndex
        )

        webTestClient
            .webPutWithAuth("/sprint-columns/${created.id}", invalidUpdateRequest)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `should delete a column`() {
        val created = createColumn()

        webTestClient
            .webDeleteWithAuth("/sprint-columns/${created.id}")
            .exchange()
            .expectStatus().isOk

        webTestClient
            .webGetWithAuth("/sprint-columns/${created.id}")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should return 404 when column not found`() {
        val nonExistentId = UUID.randomUUID()

        webTestClient
            .webGetWithAuth("/sprint-columns/$nonExistentId")
            .exchange()
            .expectStatus().isNotFound

        val updateRequest = createColumnRequest()
        webTestClient
            .webPutWithAuth("/sprint-columns/$nonExistentId", updateRequest)
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should return 404 when sprint not found for column creation`() {
        val nonExistentSprintId = UUID.randomUUID()
        val request = createColumnRequest(sprintId = nonExistentSprintId)

        webTestClient
            .webPostWithAuth("/sprint-columns", request)
            .exchange()
            .expectStatus().isNotFound
    }
}