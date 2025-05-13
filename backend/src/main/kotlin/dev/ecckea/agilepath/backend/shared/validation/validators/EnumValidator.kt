package dev.ecckea.agilepath.backend.shared.validation.validators

import dev.ecckea.agilepath.backend.shared.validation.annotations.ValidEnum
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * Validator to ensure that a string value matches one of the valid values in a specified enum.
 * This validator is used in conjunction with the `ValidEnum` annotation.
 *
 * Implements the `ConstraintValidator` interface for the `ValidEnum` annotation.
 */
class EnumValidator : ConstraintValidator<ValidEnum, String> {

    /**
     * Set of accepted values derived from the specified enum class.
     */
    private lateinit var acceptedValues: Set<String>

    /**
     * Flag indicating whether the validation should ignore case when comparing values.
     */
    private var ignoreCase: Boolean = false

    /**
     * Initializes the validator with the properties of the `ValidEnum` annotation.
     *
     * @param annotation The `ValidEnum` annotation instance containing configuration.
     */
    override fun initialize(annotation: ValidEnum) {
        ignoreCase = annotation.ignoreCase
        acceptedValues = annotation.enumClass.java.enumConstants
            .map { if (ignoreCase) it.name.lowercase() else it.name }
            .toSet()
    }

    /**
     * Validates whether the given string value matches one of the accepted enum values.
     *
     * @param value The string value to validate.
     * @param context The `ConstraintValidatorContext` to build custom constraint violations.
     * @return `true` if the value is valid, `false` otherwise.
     */
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return false
        val normalized = if (ignoreCase) value.lowercase() else value
        return acceptedValues.contains(normalized)
    }
}