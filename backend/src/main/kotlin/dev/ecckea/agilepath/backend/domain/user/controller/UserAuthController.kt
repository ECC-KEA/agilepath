package dev.ecckea.agilepath.backend.domain.user.controller

import dev.ecckea.agilepath.backend.domain.user.application.UserAuthApplication
import dev.ecckea.agilepath.backend.domain.user.dto.UserResponse
import dev.ecckea.agilepath.backend.domain.user.model.toDTO
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.security.currentUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints related to authentication and current user")
class UserAuthController(
    private val userAuthApplication: UserAuthApplication
) : Logged() {
    @Operation(
        summary = "Get current user",
        description = "Returns the authenticated user. Creates a new one if this is the first login.",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returned user profile"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "403", description = "Forbidden – Authenticated but not allowed"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/profile")
    suspend fun getProfile(): UserResponse {
        log.info("GET /auth/profile - Get current user")
        val user = userAuthApplication.getCurrentUser(currentUser())
        return user.toDTO()
    }
}