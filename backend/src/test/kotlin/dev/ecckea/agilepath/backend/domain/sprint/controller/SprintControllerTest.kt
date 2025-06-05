package dev.ecckea.agilepath.backend.domain.sprint.controller

import dev.ecckea.agilepath.backend.config.TestAppConfig
import dev.ecckea.agilepath.backend.domain.sprint.dto.SprintRequest
import dev.ecckea.agilepath.backend.domain.sprint.dto.SprintResponse
import dev.ecckea.agilepath.backend.support.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestAppConfig::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SprintControllerTest : IntegrationTestBase() {

    companion object {
        val existingProjectId: UUID = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val otherProjectId: UUID = UUID.fromString("00000000-0000-0000-0000-000000000002")
    }

    private fun createSprintRequest(
        projectId: UUID = existingProjectId,
        name: String = "Sprint-${UUID.randomUUID()}",
        goal: String = "Complete the test features",
        teamCapacity: Int = 28,
        startDate: LocalDate = LocalDate.now(),
        endDate: LocalDate = LocalDate.now().plusWeeks(2)
    ) = SprintRequest(
        projectId = projectId,
        name = name,
        goal = goal,
        teamCapacity = teamCapacity,
        startDate = startDate,
        endDate = endDate
    )

    private fun createSprint(projectId: UUID = existingProjectId): SprintResponse {
        return webTestClient
            .webPostWithAuth("/sprints", createSprintRequest(projectId))
            .exchange()
            .expectStatus().isOk
            .parseBody()
    }

    @Test
    fun `should create a sprint`() {
        val request = createSprintRequest()

        val created = webTestClient
            .webPostWithAuth("/sprints", request)
            .exchange()
            .expectStatus().isOk
            .parseBody<SprintResponse>()

        assertNotNull(created.id)
        assertEquals(request.projectId, created.projectId)
        assertEquals(request.name, created.name)
        assertEquals(request.goal, created.goal)
        assertEquals(request.startDate, created.startDate)
        assertEquals(request.endDate, created.endDate)
        assertNotNull(created.createdBy)
    }

    @Test
    fun `should fetch a sprint by id`() {
        val created = createSprint()

        val fetched = webTestClient
            .webGetWithAuth("/sprints/${created.id}")
            .exchange()
            .expectStatus().isOk
            .parseBody<SprintResponse>()

        assertEquals(created.id, fetched.id)
        assertEquals(created.name, fetched.name)
        assertEquals(created.projectId, fetched.projectId)
    }

    @Test
    fun `should fetch all sprints for a project`() {
        val sprints = webTestClient
            .webGetWithAuth("/projects/$existingProjectId/sprints")
            .exchange()
            .expectStatus().isOk
            .parseListBody<SprintResponse>()

        assertTrue(sprints.isNotEmpty(), "Expected dummy sprints to be present")
        assertTrue(sprints.all { it.projectId == existingProjectId })
    }

    @Test
    fun `should update a sprint`() {
        val created = createSprint()

        val updatedRequest = SprintRequest(
            projectId = created.projectId,
            name = "Updated Sprint Name",
            goal = "Updated goal",
            teamCapacity = 28,
            startDate = created.startDate,
            endDate = created.endDate.plusWeeks(1)
        )

        val updated = webTestClient
            .webPutWithAuth("/sprints/${created.id}", updatedRequest)
            .exchange()
            .expectStatus().isOk
            .parseBody<SprintResponse>()

        assertEquals(created.id, updated.id)
        assertEquals("Updated Sprint Name", updated.name)
        assertEquals("Updated goal", updated.goal)
        assertEquals(created.startDate, updated.startDate)
        assertEquals(created.endDate.plusWeeks(1), updated.endDate)
    }

    @Test
    fun `should not allow changing project ID during update`() {
        val sprint = createSprint()

        val invalidUpdateRequest = SprintRequest(
            projectId = otherProjectId,
            name = sprint.name,
            goal = sprint.goal,
            teamCapacity = 28,
            startDate = sprint.startDate,
            endDate = sprint.endDate
        )

        webTestClient
            .webPutWithAuth("/sprints/${sprint.id}", invalidUpdateRequest)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `should return 404 when sprint not found`() {
        val nonExistentId = UUID.randomUUID()

        webTestClient
            .webGetWithAuth("/sprints/$nonExistentId")
            .exchange()
            .expectStatus().isNotFound

        val updateRequest = createSprintRequest()
        webTestClient
            .webPutWithAuth("/sprints/$nonExistentId", updateRequest)
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should return 404 when project not found for sprint creation`() {
        val nonExistentProjectId = UUID.randomUUID()
        val request = createSprintRequest(nonExistentProjectId)

        webTestClient
            .webPostWithAuth("/sprints", request)
            .exchange()
            .expectStatus().isNotFound
    }
}