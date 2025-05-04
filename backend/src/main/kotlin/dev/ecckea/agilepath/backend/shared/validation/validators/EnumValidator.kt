package dev.ecckea.agilepath.backend.shared.validation.validators

import dev.ecckea.agilepath.backend.shared.validation.annotations.ValidEnum
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EnumValidator : ConstraintValidator<ValidEnum, String> {

    private lateinit var acceptedValues: Set<String>
    private var ignoreCase: Boolean = false

    override fun initialize(annotation: ValidEnum) {
        ignoreCase = annotation.ignoreCase
        acceptedValues = annotation.enumClass.java.enumConstants
            .map { if (ignoreCase) it.name.lowercase() else it.name }
            .toSet()
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return false
        val normalized = if (ignoreCase) value.lowercase() else value
        return acceptedValues.contains(normalized)
    }
}