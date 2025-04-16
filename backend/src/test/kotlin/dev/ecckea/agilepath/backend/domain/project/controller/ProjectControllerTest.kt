package dev.ecckea.agilepath.backend.domain.project.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dev.ecckea.agilepath.backend.domain.project.application.ProjectApplication
import dev.ecckea.agilepath.backend.domain.project.dto.ProjectResponse
import dev.ecckea.agilepath.backend.domain.project.model.Project
import dev.ecckea.agilepath.backend.domain.project.type.Framework
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.ZonedDateTime
import java.util.UUID

@WebMvcTest(ProjectController::class)
@Import(ProjectControllerTest.TestConfig::class)
class ProjectControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var projectApplication: ProjectApplication

    @Test
    fun `should create project and return 200`() = runTest {
        // Arrange
        val request = ProjectResponse(
            id = UUID.randomUUID().toString(),
            name = "Test Project",
            description = "A sample project",
            framework = Framework.SCRUM,
            createdBy = "user123",
            createdAt = ZonedDateTime.now()
        )

        val projectModel = Project(
            id = request.id,
            name = request.name,
            description = request.description,
            framework = request.framework,
            createdBy = request.createdBy,
            createdAt = request.createdAt.toInstant()
        )

        coEvery { projectApplication.createProject(any()) } returns projectModel

        // Mock JWT token with claims from SecurityConfig
        val jwt = SecurityMockMvcRequestPostProcessors.jwt()
            .jwt {
                it.claim("sub", "user123")
                it.claim("email", "user@example.com")
            }

        // Act & Assert
        mockMvc.post("/project") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
            with(jwt)
            header("Origin", "http://localhost") // Mock CORS
        }.andExpect {
            status { isOk() } // Expect 200 OK
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(objectMapper.writeValueAsString(request)) }
        }
    }

    @Configuration
    class TestConfig {
        @Bean
        fun projectApplication(): ProjectApplication = mockk()
    }
}