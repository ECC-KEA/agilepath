package dev.ecckea.agilepath.backend.shared.validation.validators

import dev.ecckea.agilepath.backend.domain.sprint.dto.SprintRequest
import dev.ecckea.agilepath.backend.shared.validation.annotations.ValidDateRange
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class DateRangeValidator : ConstraintValidator<ValidDateRange, SprintRequest> {

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