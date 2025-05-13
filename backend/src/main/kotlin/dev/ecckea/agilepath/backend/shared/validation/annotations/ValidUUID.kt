package dev.ecckea.agilepath.backend.shared.validation.annotations

import dev.ecckea.agilepath.backend.shared.validation.validators.UUIDValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * Annotation to validate that a field or parameter contains a valid UUID.
 * This annotation is used in conjunction with the `UUIDValidator` class.
 *
 * @property message The error message to be returned if validation fails. Defaults to "must be a valid UUID".
 * @property groups Allows specification of validation groups. Defaults to an empty array.
 * @property payload Can be used by clients to assign custom payload objects to a constraint. Defaults to an empty array.
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UUIDValidator::class])
annotation class ValidUUID(
    val message: String = "must be a valid UUID",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)