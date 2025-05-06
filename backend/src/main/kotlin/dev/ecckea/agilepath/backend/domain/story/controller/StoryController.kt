package dev.ecckea.agilepath.backend.domain.story.controller

import dev.ecckea.agilepath.backend.domain.story.application.StoryApplication
import dev.ecckea.agilepath.backend.domain.story.dto.StoryRequest
import dev.ecckea.agilepath.backend.domain.story.dto.StoryResponse
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toDTO
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toModel
import dev.ecckea.agilepath.backend.shared.logging.Logged
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/stories")
@Validated
@Tag(name = "Stories", description = "Endpoints related to Story management")
class StoryController(
    private val storyApplication: StoryApplication
) : Logged() {

    @Operation(
        summary = "Create a new story",
        description = "Creates a new story with the provided details",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully created story"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to create a story"
            ),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PostMapping
    fun createStory(@Valid @RequestBody storyRequest: StoryRequest): StoryResponse {
        log.info("POST /stories - Create story")
        return storyApplication.createStory(storyRequest.toModel()).toDTO()
    }

    @Operation(
        summary = "Get story by ID",
        description = "Returns the story details for the specified story ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returned story details"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to access this story"
            ),
            ApiResponse(responseCode = "404", description = "Not Found – Story with specified ID does not exist"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/{id}")
    fun getStory(@PathVariable id: UUID): StoryResponse {
        log.info("GET /stories/{} - Get story", id)
        return storyApplication.getStory(id).toDTO()
    }

    @Operation(
        summary = "Update a story",
        description = "Updates the details of an existing story",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully updated story"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to update this story"
            ),
            ApiResponse(responseCode = "404", description = "Not Found – Story with specified ID does not exist"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PutMapping("/{id}")
    fun updateStory(@PathVariable id: UUID, @Valid @RequestBody storyRequest: StoryRequest): StoryResponse {
        log.info("PUT /stories/{} - Update story", id)
        return storyApplication.updateStory(id, storyRequest.toModel()).toDTO()
    }

    @Operation(
        summary = "Delete a story",
        description = "Deletes the specified story",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully deleted story"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden – Authenticated but not allowed to delete this story"
            ),
            ApiResponse(responseCode = "404", description = "Not Found – Story with specified ID does not exist"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @DeleteMapping("/{id}")
    fun deleteStory(@PathVariable id: UUID) {
        log.info("DELETE /stories/{} - Delete story", id)
        storyApplication.deleteStory(id)
    }
}