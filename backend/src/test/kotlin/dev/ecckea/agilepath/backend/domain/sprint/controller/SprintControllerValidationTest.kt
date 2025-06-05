package dev.ecckea.agilepath.backend.domain.sprint.controller

import dev.ecckea.agilepath.backend.config.TestAppConfig
import dev.ecckea.agilepath.backend.domain.sprint.dto.SprintRequest
import dev.ecckea.agilepath.backend.support.IntegrationTestBase
import dev.ecckea.agilepath.backend.support.webPostWithAuth
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.util.*
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestAppConfig::class)
class SprintControllerValidationTest : IntegrationTestBase() {

    private val validProjectId = UUID.fromString("11111111-0000-0000-0000-000000000001")
    private val tomorrow = LocalDate.now().plusDays(1)
    private val nextWeek = LocalDate.now().plusDays(7)

    private fun postInvalidSprint(request: SprintRequest): HttpStatusCode {
        return webTestClient
            .webPostWithAuth("/sprints", request)
            .exchange()
            .expectBody()
            .returnResult()
            .status
    }

    @Test
    fun `should reject blank name`() {
        val request = SprintRequest(
            projectId = validProjectId,
            name = "   ",
            goal = "Valid goal",
            teamCapacity = 28,
            startDate = tomorrow,
            endDate = nextWeek
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidSprint(request))
    }

    @Test
    fun `should reject too short name`() {
        val request = SprintRequest(
            projectId = validProjectId,
            name = "ab",
            goal = "Valid goal",
            teamCapacity = 28,
            startDate = tomorrow,
            endDate = nextWeek
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidSprint(request))
    }

    @Test
    fun `should reject HTML in goal`() {
        val request = SprintRequest(
            projectId = validProjectId,
            name = "Valid Sprint Name",
            goal = "<script>alert('xss')</script>",
            teamCapacity = 28,
            startDate = tomorrow,
            endDate = nextWeek
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidSprint(request))
    }

    @Test
    fun `should reject null project ID using JSON`() {
        val json = """
        {
            "name": "Valid Sprint Name",
            "goal": "Valid goal",
            "teamCapacity" = 28,
            "startDate": "$tomorrow",
            "endDate": "$nextWeek"
        }
    """

        webTestClient
            .webPostWithAuth("/sprints", json)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `should reject past start date`() {
        val request = SprintRequest(
            projectId = validProjectId,
            name = "Valid Sprint Name",
            goal = "Valid goal",
            teamCapacity = 28,
            startDate = LocalDate.now().minusDays(1), // Yesterday
            endDate = nextWeek
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidSprint(request))
    }

    @Test
    fun `should reject null start date using JSON`() {
        val json = """
    {
        "projectId": "$validProjectId",
        "name": "Valid Sprint Name",
        "goal": "Valid goal",
        "teamCapacity" = 28,
        "endDate": "$nextWeek"
    }
    """

        webTestClient
            .webPostWithAuth("/sprints", json)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `should reject null end date using JSON`() {
        val json = """
    {
        "projectId": "$validProjectId",
        "name": "Valid Sprint Name",
        "goal": "Valid goal",
        "teamCapacity" = 28,
        "startDate": "$tomorrow"
    }
    """

        webTestClient
            .webPostWithAuth("/sprints", json)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `should reject end date before start date`() {
        val request = SprintRequest(
            projectId = validProjectId,
            name = "Valid Sprint Name",
            goal = "Valid goal",
            teamCapacity = 28,
            startDate = tomorrow,
            endDate = LocalDate.now() // Today (before start date)
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidSprint(request))
    }
}