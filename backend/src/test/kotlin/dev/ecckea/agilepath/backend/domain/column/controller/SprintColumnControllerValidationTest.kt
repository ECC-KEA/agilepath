package dev.ecckea.agilepath.backend.domain.column.controller

import dev.ecckea.agilepath.backend.config.TestAppConfig
import dev.ecckea.agilepath.backend.domain.column.dto.SprintColumnRequest
import dev.ecckea.agilepath.backend.domain.column.model.SprintColumnStatus
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
class SprintColumnControllerValidationTest : IntegrationTestBase() {

    private val validSprintId = UUID.fromString("11111111-0000-0000-0000-000000000001")

    private fun postInvalidSprintColumn(request: SprintColumnRequest): HttpStatusCode {
        return webTestClient
            .webPostWithAuth("/sprint-columns", request)
            .exchange()
            .expectBody()
            .returnResult()
            .status
    }

    @Test
    fun `should reject blank name`() {
        val request = SprintColumnRequest(
            sprintId = validSprintId,
            name = "   ",
            columnStatus = SprintColumnStatus.TODO,
            columnIndex = 0
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidSprintColumn(request))
    }

    @Test
    fun `should reject too long name`() {
        val request = SprintColumnRequest(
            sprintId = validSprintId,
            name = "a".repeat(101), // 101 characters (exceeds 100 limit)
            columnStatus = SprintColumnStatus.TODO,
            columnIndex = 0
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidSprintColumn(request))
    }

    @Test
    fun `should reject negative column index`() {
        val request = SprintColumnRequest(
            sprintId = validSprintId,
            name = "Valid Column Name",
            columnStatus = SprintColumnStatus.TODO,
            columnIndex = -1
        )
        assertEquals(HttpStatus.BAD_REQUEST, postInvalidSprintColumn(request))
    }

    @Test
    fun `should reject null sprint ID using JSON`() {
        val json = """
        {
            "name": "Valid Column Name",
            "columnStatus": "TODO",
            "columnIndex": 0
        }
        """

        webTestClient
            .webPostWithAuth("/sprint-columns", json)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `should reject null column status using JSON`() {
        val json = """
        {
            "sprintId": "$validSprintId",
            "name": "Valid Column Name",
            "columnIndex": 0
        }
        """

        webTestClient
            .webPostWithAuth("/sprint-columns", json)
            .exchange()
            .expectStatus().isBadRequest
    }
}