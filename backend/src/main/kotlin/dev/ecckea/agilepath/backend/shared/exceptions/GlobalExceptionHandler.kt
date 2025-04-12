package dev.ecckea.agilepath.backend.shared.exceptions

import dev.ecckea.agilepath.backend.shared.dto.ErrorResponse
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.time.ZonedDateTime

@RestControllerAdvice
class GlobalExceptionHandler : Logged() {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(ex: ResourceNotFoundException): ResponseEntity<ErrorResponse> =
        errorResponse(HttpStatus.NOT_FOUND, ex.message)

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(ex: BadRequestException): ResponseEntity<ErrorResponse> =
        errorResponse(HttpStatus.BAD_REQUEST, ex.message)

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(ex: UnauthorizedException): ResponseEntity<ErrorResponse> =
        errorResponse(HttpStatus.UNAUTHORIZED, ex.message)

    @ExceptionHandler(ConflictException::class)
    fun handleConflict(ex: ConflictException): ResponseEntity<ErrorResponse> =
        errorResponse(HttpStatus.CONFLICT, ex.message)

    @ExceptionHandler(ValidationException::class)
    fun handleValidation(ex: ValidationException): ResponseEntity<ErrorResponse> =
        errorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.message)

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> =
        errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred")
            .also { log.error("Unexpected error occurred at ${request.getDescription(false)}: ${ex.message}", ex) }

    private fun errorResponse(status: HttpStatus, message: String?): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = status.value(),
            message = message ?: "An error occurred",
            timestamp = ZonedDateTime.now()
        )
        return ResponseEntity.status(status).body(error)
    }
}