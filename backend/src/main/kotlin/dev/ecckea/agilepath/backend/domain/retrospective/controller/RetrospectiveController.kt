package dev.ecckea.agilepath.backend.domain.retrospective.controller

import dev.ecckea.agilepath.backend.domain.retrospective.application.RetrospectiveApplication
import dev.ecckea.agilepath.backend.domain.retrospective.dto.RetrospectiveRequest
import dev.ecckea.agilepath.backend.domain.retrospective.dto.RetrospectiveResponse
import dev.ecckea.agilepath.backend.domain.retrospective.model.mapper.toDTO
import dev.ecckea.agilepath.backend.domain.retrospective.model.mapper.toModel
import dev.ecckea.agilepath.backend.shared.logging.Logged
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@Validated
@Tag(name = "Retrospective", description = "Endpoints related to retrospective management")
class RetrospectiveController(
    private val retrospectiveApplication: RetrospectiveApplication
) : Logged() {

    @Operation(
        summary = "Get retrospective by sprint ID",
        description = "Returns the retrospective details for the specified sprint ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returned retrospective details"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to access this project"
            ),
            ApiResponse(responseCode = "404", description = "Not Found – Project with specified ID does not exist"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/sprints/{sprintId}/retrospective")
    fun getRetrospective(@PathVariable sprintId: UUID) : RetrospectiveResponse {
        log.info("GET /sprints/{}/retrospective - Get retrospective for sprint", sprintId)
        return retrospectiveApplication.getRetrospective(sprintId).toDTO()
    }

    @Operation(
        summary = "Create new retrospective",
        description = "Creates a new retrospective with the provided details",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully created retrospective"),
            ApiResponse(responseCode = "400", description = "Bad Request – Invalid retrospective data"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to create retrospectives for this sprint"
            ),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PostMapping("/retrospectives")
    fun createRetrospective(@Valid @RequestBody retrospectiveRequest: RetrospectiveRequest): RetrospectiveResponse {
        log.info("POST /retrospectives - Create new retrospective")
        return retrospectiveApplication.createRetrospective(retrospectiveRequest.toModel()).toDTO()
    }



}