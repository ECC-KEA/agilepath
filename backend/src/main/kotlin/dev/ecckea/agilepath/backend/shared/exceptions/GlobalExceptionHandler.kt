package dev.ecckea.agilepath.backend.shared.exceptions

import dev.ecckea.agilepath.backend.shared.dto.ErrorResponse
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.utils.nowInZone
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

/**
 * Global exception handler for the application.
 * This class handles various exceptions thrown by the application and provides
 * appropriate HTTP responses with error details.
 */
@RestControllerAdvice
class GlobalExceptionHandler : Logged() {

    /**
     * Handles ResourceNotFoundException and returns a 404 NOT FOUND response.
     *
     * @param ex The exception instance.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(ex: ResourceNotFoundException): ResponseEntity<ErrorResponse> =
        errorResponse(HttpStatus.NOT_FOUND, ex.message)

    /**
     * Handles BadRequestException and returns a 400 BAD REQUEST response.
     *
     * @param ex The exception instance.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(ex: BadRequestException): ResponseEntity<ErrorResponse> =
        errorResponse(HttpStatus.BAD_REQUEST, ex.message)

    /**
     * Handles UnauthorizedException and returns a 401 UNAUTHORIZED response.
     *
     * @param ex The exception instance.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(ex: UnauthorizedException): ResponseEntity<ErrorResponse> =
        errorResponse(HttpStatus.UNAUTHORIZED, ex.message)

    /**
     * Handles ConflictException and returns a 409 CONFLICT response.
     *
     * @param ex The exception instance.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(ConflictException::class)
    fun handleConflict(ex: ConflictException): ResponseEntity<ErrorResponse> =
        errorResponse(HttpStatus.CONFLICT, ex.message)

    /**
     * Handles ValidationException and returns a 422 UNPROCESSABLE ENTITY response.
     *
     * @param ex The exception instance.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(ValidationException::class)
    fun handleValidation(ex: ValidationException): ResponseEntity<ErrorResponse> =
        errorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.message)

    /**
     * Handles generic exceptions and returns a 500 INTERNAL SERVER ERROR response.
     * Logs the error details for debugging purposes.
     *
     * @param ex The exception instance.
     * @param request The web request during which the exception occurred.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> =
        errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred")
            .also { log.error("Unexpected error occurred at ${request.getDescription(false)}: ${ex.message}", ex) }

    /**
     * Handles MethodArgumentTypeMismatchException and returns a 400 BAD REQUEST response.
     * Provides details about the invalid parameter and expected type.
     *
     * @param ex The exception instance.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(ex: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse> {
        val param = ex.name
        val expectedType = ex.requiredType?.simpleName ?: "unknown"
        val value = ex.value?.toString() ?: "null"
        val message = "Invalid value for parameter '$param': '$value' is not a valid $expectedType"

        return errorResponse(HttpStatus.BAD_REQUEST, message)
    }

    /**
     * Handles ConstraintViolationException and returns a 400 BAD REQUEST response.
     * Aggregates all constraint violation messages into a single response.
     *
     * @param ex The exception instance.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        val message = ex.constraintViolations.joinToString("; ") {
            "${it.propertyPath.last().name}: ${it.message}"
        }

        return errorResponse(HttpStatus.BAD_REQUEST, message)
    }

    /**
     * Helper method to create an error response.
     *
     * @param status The HTTP status to be returned.
     * @param message The error message to include in the response.
     * @return ResponseEntity containing the error response.
     */
    private fun errorResponse(status: HttpStatus, message: String?): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = status.value(),
            message = message ?: "An error occurred",
            timestamp = nowInZone()
        )
        return ResponseEntity.status(status).body(error)
    }
}