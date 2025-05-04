package dev.ecckea.agilepath.backend.shared.validation.validators

import dev.ecckea.agilepath.backend.shared.validation.annotations.NoHtml
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class NoHtmlValidator : ConstraintValidator<NoHtml, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return value == null || !value.contains(Regex("<[^>]++>"))
    }
}