package dev.ecckea.agilepath.backend.shared.validation.annotations

import dev.ecckea.agilepath.backend.shared.validation.validators.TrimmedNotBlankValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * Annotation to validate that a field is not blank or only whitespace after trimming.
 * This annotation is used in conjunction with the `TrimmedNotBlankValidator` class.
 *
 * @property message The error message to be returned if validation fails. Defaults to "must not be blank or only whitespace".
 * @property groups Allows specification of validation groups. Defaults to an empty array.
 * @property payload Can be used by clients to assign custom payload objects to a constraint. Defaults to an empty array.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [TrimmedNotBlankValidator::class])
annotation class TrimmedNotBlank(
    val message: String = "must not be blank or only whitespace",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)