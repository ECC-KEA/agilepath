package dev.ecckea.agilepath.backend.shared.validation.validators

import dev.ecckea.agilepath.backend.domain.story.dto.TaskRequest
import dev.ecckea.agilepath.backend.shared.validation.annotations.RequireOneEstimate
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class RequireOneEstimateValidator : ConstraintValidator<RequireOneEstimate, TaskRequest> {

    override fun isValid(value: TaskRequest?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return true

        // Check if one and only one estimate is provided
        val hasTshirtEstimate = !value.estimateTshirt.isNullOrBlank()
        val hasPointEstimate = !value.estimatePoints.isNullOrBlank()

        return hasTshirtEstimate xor hasPointEstimate
    }
}