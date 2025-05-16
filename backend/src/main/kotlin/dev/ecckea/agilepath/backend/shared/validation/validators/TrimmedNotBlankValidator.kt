package dev.ecckea.agilepath.backend.shared.validation.validators

import dev.ecckea.agilepath.backend.shared.validation.annotations.TrimmedNotBlank
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * Validator to ensure that a string is not blank after trimming whitespace.
 * This validator is used in conjunction with the `TrimmedNotBlank` annotation.
 *
 * Implements the `ConstraintValidator` interface for the `TrimmedNotBlank` annotation.
 */
class TrimmedNotBlankValidator : ConstraintValidator<TrimmedNotBlank, String> {

    /**
     * Validates whether the given string value is not blank after trimming.
     *
     * @param value The string value to validate.
     * @param context The `ConstraintValidatorContext` to build custom constraint violations.
     * @return `true` if the value is not null, trimmed, and not empty; `false` otherwise.
     */
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return value?.trim()?.isNotEmpty() ?: false
    }
}