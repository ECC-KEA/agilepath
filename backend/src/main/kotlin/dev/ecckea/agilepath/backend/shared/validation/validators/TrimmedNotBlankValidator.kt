package dev.ecckea.agilepath.backend.shared.validation.validators

import dev.ecckea.agilepath.backend.shared.validation.annotations.TrimmedNotBlank
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class TrimmedNotBlankValidator : ConstraintValidator<TrimmedNotBlank, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return value?.trim()?.isNotEmpty() ?: false
    }
}