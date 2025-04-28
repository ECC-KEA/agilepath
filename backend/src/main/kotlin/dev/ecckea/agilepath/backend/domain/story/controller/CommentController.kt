package dev.ecckea.agilepath.backend.domain.story.controller

import dev.ecckea.agilepath.backend.domain.story.application.CommentApplication
import dev.ecckea.agilepath.backend.domain.story.dto.CommentRequest
import dev.ecckea.agilepath.backend.domain.story.dto.CommentResponse
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toDTO
import dev.ecckea.agilepath.backend.domain.story.model.mapper.toModel
import dev.ecckea.agilepath.backend.shared.logging.Logged
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/comments")
@Tag(name = "Comments", description = "Endpoints related to Comment management")
class CommentController(
    private val commentApplication: CommentApplication
) : Logged() {

    @Operation(
        summary = "Create a new comment",
        description = "Creates a new comment for a story or task",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully created comment"),
            ApiResponse(responseCode = "400", description = "Bad Request - Invalid comment data"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "403", description = "Forbidden – Not allowed to create a comment"),
            ApiResponse(responseCode = "404", description = "Not Found – Story or task not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PostMapping
    fun createComment(@RequestBody commentRequest: CommentRequest): CommentResponse {
        log.info("POST /comments - Create comment")
        return commentApplication.createComment(commentRequest.toModel()).toDTO()
    }

    @Operation(
        summary = "Get comment by ID",
        description = "Returns the comment with the specified ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returned comment"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "403", description = "Forbidden – Not allowed to access this comment"),
            ApiResponse(responseCode = "404", description = "Not Found – Comment not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/{id}")
    fun getComment(@PathVariable id: UUID): CommentResponse {
        log.info("GET /comments/$id - Get comment by ID")
        return commentApplication.getComment(id).toDTO()
    }

    @Operation(
        summary = "Get comments by task ID",
        description = "Returns all comments for the specified task",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returned comments"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/task/{taskId}")
    fun getCommentsByTaskId(@PathVariable taskId: UUID): List<CommentResponse> {
        log.info("GET /comments/task/$taskId - Get comments by task ID")
        return commentApplication.getCommentsByTaskId(taskId).map { it.toDTO() }
    }

    @Operation(
        summary = "Get comments by story ID",
        description = "Returns all comments for the specified story",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully returned comments"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @GetMapping("/story/{storyId}")
    fun getCommentsByStoryId(@PathVariable storyId: UUID): List<CommentResponse> {
        log.info("GET /comments/story/$storyId - Get comments by story ID")
        return commentApplication.getCommentsByStoryId(storyId).map { it.toDTO() }
    }

    @Operation(
        summary = "Update comment by ID",
        description = "Updates the comment with the specified ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully updated comment"),
            ApiResponse(responseCode = "400", description = "Bad Request - Invalid comment data"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "403", description = "Forbidden – Not allowed to update this comment"),
            ApiResponse(responseCode = "404", description = "Not Found – Comment not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @PutMapping("/{id}")
    fun updateComment(@PathVariable id: UUID, @RequestBody commentRequest: CommentRequest): CommentResponse {
        log.info("PUT /comments/$id - Update comment")
        return commentApplication.updateComment(id, commentRequest.toModel()).toDTO()
    }

    @Operation(
        summary = "Delete comment by ID",
        description = "Deletes the comment with the specified ID",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Successfully deleted comment"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "403", description = "Forbidden – Not allowed to delete this comment"),
            ApiResponse(responseCode = "404", description = "Not Found – Comment not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteComment(@PathVariable id: UUID) {
        log.info("DELETE /comments/$id - Delete comment")
        commentApplication.deleteComment(id)
    }
}