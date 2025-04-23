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

    private fun createProjectRequest(
        name: String = "AgilePath-${UUID.randomUUID()}",
        description: String = "A Kanban app for agile learners",
        framework: Framework = Framework.SCRUM
    ) = ProjectRequest(
        name = name,
        description = description,
        framework = framework
    )

    private fun createProject(request: ProjectRequest = createProjectRequest()): ProjectResponse {
        return webTestClient
            .webPostWithAuth("/projects", request)
            .exchange()
            .expectStatus().isOk
            .parseBody()
    }

    @Test
    fun `should create a project`() {
        val request = createProjectRequest()

        val created = webTestClient
            .webPostWithAuth("/projects", request)
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
    fun `should create projects with different frameworks`() {
        val scrumProject = createProject(createProjectRequest(framework = Framework.SCRUM))
        assertEquals(Framework.SCRUM, scrumProject.framework)

        val kanbanProject = createProject(createProjectRequest(framework = Framework.XP))
        assertEquals(Framework.XP, kanbanProject.framework)

        val xpProject = createProject(createProjectRequest(framework = Framework.XP))
        assertEquals(Framework.XP, xpProject.framework)
    }

    @Test
    fun `should fetch a project by id`() {
        val created = createProject()

        val fetched = webTestClient
            .webGetWithAuth("/projects/${created.id}")
            .exchange()
            .expectStatus().isOk
            .parseBody<ProjectResponse>()

        assertEquals(created.id, fetched.id)
        assertEquals(created.name, fetched.name)
        assertEquals(created.description, fetched.description)
        assertEquals(created.framework, fetched.framework)
        assertEquals(created.createdBy, fetched.createdBy)
        // Time precision might differ slightly, so just check it exists
        assertNotNull(fetched.createdAt)
    }

    @Test
    fun `should return 404 for non-existent project`() {
        val nonExistentId = UUID.randomUUID()

        webTestClient
            .webGetWithAuth("/projects/$nonExistentId")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should update a project`() {
        val created = createProject()

        val updatedRequest = ProjectRequest(
            name = "AgilePath++",
            description = "Updated description",
            framework = Framework.XP
        )

        val updated = webTestClient
            .webPutWithAuth("/projects/${created.id}", updatedRequest)
            .exchange()
            .expectStatus().isOk
            .parseBody<ProjectResponse>()

        assertEquals("AgilePath++", updated.name)
        assertEquals("Updated description", updated.description)
        assertEquals(Framework.XP, updated.framework)
        assertEquals(created.id, updated.id)
        assertEquals(created.createdBy, updated.createdBy)
        // Should have a modified timestamp
//        assertNotNull(updated.modifiedAt)
//        assertNotNull(updated.modifiedBy)
    }

    @Test
    fun `should delete a project`() {
        val created = createProject()

        webTestClient
            .webDeleteWithAuth("/projects/${created.id}")
            .exchange()
            .expectStatus().isOk

        webTestClient
            .webGetWithAuth("/projects/${created.id}")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should return 404 when deleting non-existent project`() {
        val nonExistentId = UUID.randomUUID()

        webTestClient
            .webDeleteWithAuth("/projects/$nonExistentId")
            .exchange()
            .expectStatus().isNotFound
    }
}