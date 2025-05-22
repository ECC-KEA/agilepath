package dev.ecckea.agilepath.backend.domain.project.controller

import dev.ecckea.agilepath.backend.config.TestAppConfig
import dev.ecckea.agilepath.backend.domain.project.dto.ProjectMemberRequest
import dev.ecckea.agilepath.backend.domain.project.dto.ProjectMemberResponse
import dev.ecckea.agilepath.backend.domain.project.model.ProjectRole
import dev.ecckea.agilepath.backend.support.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.util.*
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestAppConfig::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectMembershipControllerTest : IntegrationTestBase() {

    companion object {
        private val projectId1 = UUID.fromString("00000000-0000-0000-0000-000000000001")
        private val projectId2 = UUID.fromString("00000000-0000-0000-0000-000000000002")
        private const val dummyUser1 = "dummy-user-1"
        private const val dummyUser2 = "dummy-user-2"
        private const val dummyUser3 = "dummy-user-3"
    }

    @Test
    fun `should add a member to the project`() {
        val request = ProjectMemberRequest(
            userId = dummyUser3,
            role = "CONTRIBUTOR"
        )

        webTestClient
            .webPostWithAuth("/projects/$projectId1/members", request)
            .exchange()
            .expectStatus().isOk

        val members = webTestClient
            .webGetWithAuth("/projects/$projectId1/members")
            .exchange()
            .expectStatus().isOk
            .parseListBody<ProjectMemberResponse>()

        assertTrue(members.any { it.user.id == dummyUser3 && it.role == ProjectRole.CONTRIBUTOR })
    }

    @Test
    fun `should add a member with case-insensitive role`() {
        val request = ProjectMemberRequest(
            userId = dummyUser3,
            role = "contributor" // lower case
        )

        webTestClient.webPostWithAuth("/projects/$projectId2/members", request)
            .exchange()
            .expectStatus().isOk

        val members = webTestClient
            .webGetWithAuth("/projects/$projectId2/members")
            .exchange()
            .expectStatus().isOk
            .parseListBody<ProjectMemberResponse>()

        assertTrue(members.any { it.user.id == dummyUser3 && it.role == ProjectRole.CONTRIBUTOR })
    }

    @Test
    fun `should update a project member's role`() {
        // dummy-user-3 is CONTRIBUTOR in this project
        webTestClient
            .webPutWithAuth("/projects/$projectId1/members/$dummyUser3?role=ADMIN", body = "")
            .exchange()
            .expectStatus().isOk

        val members = webTestClient
            .webGetWithAuth("/projects/$projectId1/members")
            .exchange()
            .expectStatus().isOk
            .parseListBody<ProjectMemberResponse>()

        assertTrue(members.any { it.user.id == dummyUser3 && it.role == ProjectRole.ADMIN })
    }

    @Test
    fun `should remove a member from the project`() {
        // dummy-user-2 is in the project
        webTestClient
            .webDeleteWithAuth("/projects/$projectId1/members/$dummyUser2")
            .exchange()
            .expectStatus().isOk

        val members = webTestClient
            .webGetWithAuth("/projects/$projectId1/members")
            .exchange()
            .expectStatus().isOk
            .parseListBody<ProjectMemberResponse>()

        assertTrue(members.none { it.user.id == dummyUser2 })
    }

    @Test
    fun `should fail to add a member with an invalid role`() {
        val request = ProjectMemberRequest(userId = dummyUser3, role = "CEO")

        webTestClient.webPostWithAuth("/projects/$projectId1/members", request)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `should fail to add a member with blank userId`() {
        val request = ProjectMemberRequest(userId = "  ", role = "CONTRIBUTOR")

        webTestClient.webPostWithAuth("/projects/$projectId1/members", request)
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `should return not found when updating role for non-member`() {
        webTestClient.webPutWithAuth("/projects/$projectId1/members/non-existent-user?role=ADMIN", body = "")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should return not found when removing non-member`() {
        webTestClient.webDeleteWithAuth("/projects/$projectId1/members/non-existent-user")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should return 400 for invalid UUID`() {
        webTestClient.webGetWithAuth("/projects/not-a-uuid/members")
            .exchange()
            .expectStatus().isBadRequest
    }

    @Test
    fun `should return 404 when accessing a non-existent project`() {
        val nonExistentId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        webTestClient.webGetWithAuth("/projects/$nonExistentId/members")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should return 401 for unauthorized access`() {
        webTestClient.get().uri("/projects/$projectId1/members")
            .exchange()
            .expectStatus().isUnauthorized
    }

    @Test
    fun `should confirm seeded member role is correct`() {
        val members = webTestClient.webGetWithAuth("/projects/$projectId1/members")
            .exchange()
            .expectStatus().isOk
            .parseListBody<ProjectMemberResponse>()

        assertEquals(ProjectRole.OWNER, members.first { it.user.id == dummyUser1 }.role)
        assertEquals(ProjectRole.CONTRIBUTOR, members.first { it.user.id == dummyUser2 }.role)
    }
}
