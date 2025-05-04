package dev.ecckea.agilepath.backend.shared.validation.validators

import dev.ecckea.agilepath.backend.shared.validation.annotations.ValidUUID
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.*

class UUIDValidator : ConstraintValidator<ValidUUID, String> {

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