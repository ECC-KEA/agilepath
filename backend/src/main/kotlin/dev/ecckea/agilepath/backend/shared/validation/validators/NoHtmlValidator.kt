package dev.ecckea.agilepath.backend.shared.validation.validators

import dev.ecckea.agilepath.backend.shared.validation.annotations.NoHtml
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * Validator to ensure that a string does not contain HTML tags.
 * This validator is used in conjunction with the `NoHtml` annotation.
 *
 * Implements the `ConstraintValidator` interface for the `NoHtml` annotation.
 */
class NoHtmlValidator : ConstraintValidator<NoHtml, String> {

    /**
     * Validates whether the given string value does not contain HTML tags.
     *
     * @param value The string value to validate.
     * @param context The `ConstraintValidatorContext` to build custom constraint violations.
     * @return `true` if the value is null or does not contain HTML tags, `false` otherwise.
     */
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return value == null || !value.contains(Regex("<[^>]++>"))
    }
}