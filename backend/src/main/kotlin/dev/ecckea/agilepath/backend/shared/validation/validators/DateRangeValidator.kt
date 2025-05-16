package dev.ecckea.agilepath.backend.shared.validation.validators

import dev.ecckea.agilepath.backend.domain.sprint.dto.SprintRequest
import dev.ecckea.agilepath.backend.shared.validation.annotations.ValidDateRange
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * Validator to ensure that a `SprintRequest` object has a valid date range.
 * This validator checks that the `endDate` is after the `startDate`.
 *
 * Implements the `ConstraintValidator` interface for the `ValidDateRange` annotation.
 */
class DateRangeValidator : ConstraintValidator<ValidDateRange, SprintRequest> {

    /**
     * Validates the date range of a `SprintRequest` object.
     *
     * @param value The `SprintRequest` object to validate.
     * @param context The `ConstraintValidatorContext` to build custom constraint violations.
     * @return `true` if the `endDate` is after the `startDate`, `false` otherwise.
     */
    override fun isValid(value: SprintRequest, context: ConstraintValidatorContext): Boolean {
        // Check if endDate is after startDate
        if (!value.endDate.isAfter(value.startDate)) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate("End date must be after start date")
                .addPropertyNode("endDate")
                .addConstraintViolation()
            return false
        }

        return true
    }
}