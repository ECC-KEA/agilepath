package dev.ecckea.agilepath.backend.shared.validation.annotations

import dev.ecckea.agilepath.backend.shared.validation.validators.DateRangeValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [DateRangeValidator::class])
annotation class ValidDateRange(
    val message: String = "End date must be after start date",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)