package dev.ecckea.agilepath.backend.shared.exceptions

import dev.ecckea.agilepath.backend.shared.dto.ErrorResponse
import dev.ecckea.agilepath.backend.shared.utils.nowInZone
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

@RestControllerAdvice
class GlobalExceptionHandler {

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
    fun handleGeneric(ex: Exception): ResponseEntity<ErrorResponse> =
        errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.message)

    private fun errorResponse(status: HttpStatus, message: String?): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = status.value(),
            message = message ?: "An error occurred",
            timestamp = nowInZone()
        )
        return ResponseEntity.status(status).body(error)
    }
}