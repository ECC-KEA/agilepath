package dev.ecckea.agilepath.backend.shared.validation.annotations

import dev.ecckea.agilepath.backend.shared.validation.validators.DateRangeValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * Annotation to validate that a class has a valid date range.
 * This annotation ensures that the end date is after the start date.
 * It is used in conjunction with the `DateRangeValidator` class.
 *
 * @property message The error message to be returned if validation fails. Defaults to "End date must be after start date".
 * @property groups Allows specification of validation groups. Defaults to an empty array.
 * @property payload Can be used by clients to assign custom payload objects to a constraint. Defaults to an empty array.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [DateRangeValidator::class])
annotation class ValidDateRange(
    val message: String = "End date must be after start date",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)