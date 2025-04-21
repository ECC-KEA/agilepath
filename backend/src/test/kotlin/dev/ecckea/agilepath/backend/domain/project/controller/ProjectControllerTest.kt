package dev.ecckea.agilepath.backend.domain.project.controller

import dev.ecckea.agilepath.backend.domain.project.dto.ProjectResponse
import dev.ecckea.agilepath.backend.domain.project.type.Framework
import dev.ecckea.agilepath.backend.shared.utils.nowInZone
import dev.ecckea.agilepath.backend.support.*
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ProjectControllerTest : IntegrationTestBase() {

    @Test
    fun `should create, fetch, update and delete a project`() {
        // 1. Create
        val request = ProjectResponse(
            id = UUID.randomUUID().toString(),
            name = "AgilePath",
            description = "A Kanban app for agile learners",
            framework = Framework.SCRUM,
            createdBy = "user-id",
            createdAt = nowInZone()
        )

        val created = webTestClient
            .webPost("/project", request)
            .exchange()
            .expectStatus().isOk
            .parseBody<ProjectResponse>()

        assertEquals(request.name, created.name)
        assertEquals(request.description, created.description)
        assertEquals(request.framework, created.framework)
        assertNotNull(created.id)

        // 2. Fetch
        val fetched = webTestClient
            .webGetWithAuth("/project/${created.id}")
            .exchange()
            .expectStatus().isOk
            .parseBody<ProjectResponse>()

        assertEquals(created.id, fetched.id)

        // 3. Update
        val updatedRequest = created.copy(name = "AgilePath++")

        val updated = webTestClient
            .webPut("/project/${created.id}", updatedRequest)
            .exchange()
            .expectStatus().isOk
            .parseBody<ProjectResponse>()

        assertEquals("AgilePath++", updated.name)

        // 4. Delete
        webTestClient
            .webDelete("/project/${created.id}")
            .exchange()
            .expectStatus().isOk

        // 5. Verify deletion
        webTestClient
            .webGetWithAuth("/project/${created.id}")
            .exchange()
            .expectStatus().isNotFound
    }
}
