package dev.ecckea.agilepath.backend.shared.validation.annotations

import dev.ecckea.agilepath.backend.shared.validation.validators.EnumValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * Annotation to validate that a field contains a valid value from a specified enum class.
 * This annotation is used in conjunction with the `EnumValidator` class.
 *
 * @property enumClass The enum class that defines the valid values for the field.
 * @property ignoreCase Indicates whether the validation should ignore case when comparing values. Defaults to false.
 * @property message The error message to be returned if validation fails. Defaults to "must be a valid enum value".
 * @property groups Allows specification of validation groups. Defaults to an empty array.
 * @property payload Can be used by clients to assign custom payload objects to a constraint. Defaults to an empty array.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [EnumValidator::class])
annotation class ValidEnum(
    val enumClass: KClass<out Enum<*>>,
    val ignoreCase: Boolean = false,
    val message: String = "must be a valid enum value",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)