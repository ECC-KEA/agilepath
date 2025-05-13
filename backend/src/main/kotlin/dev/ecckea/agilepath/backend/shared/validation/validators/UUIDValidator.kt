package dev.ecckea.agilepath.backend.shared.validation.validators

import dev.ecckea.agilepath.backend.shared.validation.annotations.ValidUUID
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.*

/**
 * Validator to ensure that a string is a valid UUID.
 * This validator is used in conjunction with the `ValidUUID` annotation.
 *
 * Implements the `ConstraintValidator` interface for the `ValidUUID` annotation.
 */
class UUIDValidator : ConstraintValidator<ValidUUID, String> {

    /**
     * Validates whether the given string value is a valid UUID.
     *
     * @param value The string value to validate.
     * @param context The `ConstraintValidatorContext` to build custom constraint violations.
     * @return `true` if the value is a valid UUID, `false` otherwise.
     */
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value.isNullOrBlank()) return false
        return try {
            UUID.fromString(value)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}