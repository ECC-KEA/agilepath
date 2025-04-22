package dev.ecckea.agilepath.backend.domain.project.controller

import dev.ecckea.agilepath.backend.config.TestAppConfig
import dev.ecckea.agilepath.backend.domain.project.dto.ProjectRequest
import dev.ecckea.agilepath.backend.domain.project.dto.ProjectResponse
import dev.ecckea.agilepath.backend.domain.project.model.Framework
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
class ProjectControllerTest : IntegrationTestBase() {

    private fun createProjectRequest(name: String = "AgilePath-${UUID.randomUUID()}") = ProjectRequest(
        name = name,
        description = "A Kanban app for agile learners",
        framework = Framework.SCRUM
    )

    private fun createProject(): ProjectResponse {
        return webTestClient
            .webPostWithAuth("/project", createProjectRequest())
            .exchange()
            .expectStatus().isOk
            .parseBody()
    }

    @Test
    fun `should create a project`() {
        val request = createProjectRequest()

        val created = webTestClient
            .webPostWithAuth("/project", request)
            .exchange()
            .expectStatus().isOk
            .parseBody<ProjectResponse>()

        assertNotNull(created.id)
        assertEquals(request.name, created.name)
        assertEquals(request.description, created.description)
        assertEquals(request.framework, created.framework)
        assertNotNull(created.createdBy)
        assertNotNull(created.createdAt)
    }

    @Test
    fun `should fetch a project by id`() {
        val created = createProject()

        val fetched = webTestClient
            .webGetWithAuth("/project/${created.id}")
            .exchange()
            .expectStatus().isOk
            .parseBody<ProjectResponse>()

        assertEquals(created.id, fetched.id)
        assertEquals(created.name, fetched.name)
    }

    @Test
    fun `should update a project`() {
        val created = createProject()

        val updatedRequest = ProjectRequest(
            name = "AgilePath++",
            description = created.description,
            framework = created.framework
        )

        val updated = webTestClient
            .webPutWithAuth("/project/${created.id}", updatedRequest)
            .exchange()
            .expectStatus().isOk
            .parseBody<ProjectResponse>()

        assertEquals("AgilePath++", updated.name)
        assertEquals(created.id, updated.id)
    }

    @Test
    fun `should delete a project`() {
        val created = createProject()

        webTestClient
            .webDeleteWithAuth("/project/${created.id}")
            .exchange()
            .expectStatus().isOk

        webTestClient
            .webGetWithAuth("/project/${created.id}")
            .exchange()
            .expectStatus().isNotFound
    }
}
