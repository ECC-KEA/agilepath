package dev.ecckea.agilepath.backend.shared.exceptions

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import dev.ecckea.agilepath.backend.shared.dto.ErrorResponse
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.utils.nowInZone
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
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

    @ExceptionHandler(ResourceNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(ex: ResourceNotFoundException) = createErrorResponse(
        HttpStatus.NOT_FOUND,
        ex.message
    )

    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequest(ex: BadRequestException) = createErrorResponse(
        HttpStatus.BAD_REQUEST,
        ex.message
    )

    @ExceptionHandler(UnauthorizedException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleUnauthorized(ex: UnauthorizedException) = createErrorResponse(
        HttpStatus.UNAUTHORIZED,
        ex.message
    )

    @ExceptionHandler(ConflictException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleConflict(ex: ConflictException) = createErrorResponse(
        HttpStatus.CONFLICT,
        ex.message
    )

    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun handleValidation(ex: ValidationException) = createErrorResponse(
        HttpStatus.UNPROCESSABLE_ENTITY,
        ex.message
    )

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGeneric(ex: Exception, request: WebRequest) = createErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "An unexpected error occurred: ${ex.message}"
    ).also { log.error("Unexpected error occurred at ${request.getDescription(false)}: ${ex.message}", ex) }


    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleTypeMismatch(ex: MethodArgumentTypeMismatchException) = createErrorResponse(
        HttpStatus.BAD_REQUEST,
        "Invalid value for parameter '${ex.name}': '${ex.value}' is not a valid ${ex.requiredType?.simpleName}"
    )


    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolation(ex: ConstraintViolationException) = createErrorResponse(
        HttpStatus.BAD_REQUEST,
        "Contraint violation occured: ${ex.constraintViolations.joinToString("; ") { "${it.propertyPath.last().name}: ${it.message}" }}"
    )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ErrorResponse {
        val errors = formatValidationErrors(ex)
        log.warn("Validation error for request ${request.method} ${request.requestURI}: ${ex.message}")
        return createErrorResponse(HttpStatus.BAD_REQUEST, errors)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException): ErrorResponse {
        // Extract meaningful error message from the exception
        val message = when {
            ex.cause is MismatchedInputException -> {
                val cause = ex.cause as MismatchedInputException
                val fieldName = cause.path.lastOrNull()?.fieldName ?: "unknown field"
                "Required field '$fieldName' is missing"
            }

            ex.message?.contains("JSON parse error") == true -> {
                "Invalid JSON format: ${ex.message?.substringAfter("JSON parse error:")?.trim()}"
            }

            else -> {
                "Invalid request body: ${ex.message}"
            }
        }

        return createErrorResponse(HttpStatus.BAD_REQUEST, message)
    }

    private fun createErrorResponse(status: HttpStatus, message: String?): ErrorResponse =
        ErrorResponse(
            status = status.value(),
            message = message ?: "An error occurred",
            timestamp = nowInZone()
        )

    /**
     * Helper method to format validation errors from MethodArgumentNotValidException.
     */
    private fun formatValidationErrors(ex: MethodArgumentNotValidException): String {
        val errors = ex.bindingResult.fieldErrors.joinToString("; ") {
            "${it.field}: ${it.defaultMessage}"
        }

        return if (errors.isNotEmpty()) "Validation failed: $errors" else "Validation failed"
    }
}