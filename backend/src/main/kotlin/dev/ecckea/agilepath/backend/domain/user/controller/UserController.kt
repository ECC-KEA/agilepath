package dev.ecckea.agilepath.backend.domain.user.controller

import dev.ecckea.agilepath.backend.domain.user.application.UserAuthApplication
import dev.ecckea.agilepath.backend.domain.user.application.UserApplication
import dev.ecckea.agilepath.backend.domain.user.dto.UserResponse
import dev.ecckea.agilepath.backend.domain.user.model.mapper.toDTO
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PathVariable

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Endpoints related to users")
class UserController (
    private val userApplication: UserApplication
) : Logged() {

  @Operation(
    summary = "Get users by search",
    description = "Returns a list of users that match the search criteria.",
    security = [SecurityRequirement(name = "bearerAuth")]
  )
  @ApiResponses(
      value = [
          ApiResponse(responseCode = "200", description = "Successfully returned users"),
          ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
          ApiResponse(responseCode = "403", description = "Forbidden – Authenticated but not allowed"),
          ApiResponse(responseCode = "500", description = "Internal server error")
      ]
  )
  // query param search
  @GetMapping
  fun getUsersBySearch(
      @RequestParam q: String
  ): List<UserResponse> {
      return userApplication.getUsersBySearch(q).map { it.toDTO() }
  }

  @Operation(
    summary = "Get user by id",
    description = "Returns a user by id.",
    security = [SecurityRequirement(name = "bearerAuth")]
  )
  @ApiResponses(
      value = [
          ApiResponse(responseCode = "200", description = "Successfully returned user"),
          ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
          ApiResponse(responseCode = "403", description = "Forbidden – Authenticated but not allowed"),
          ApiResponse(responseCode = "500", description = "Internal server error")
      ]
  )
  @GetMapping("/{id}")
  fun getUserById(
      @PathVariable id: String
  ): UserResponse {
      return userApplication.getUserById(id).toDTO()
  }
}