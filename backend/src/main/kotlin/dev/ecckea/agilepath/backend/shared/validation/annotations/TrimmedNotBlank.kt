package dev.ecckea.agilepath.backend.shared.validation.annotations

import dev.ecckea.agilepath.backend.shared.validation.validators.TrimmedNotBlankValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [TrimmedNotBlankValidator::class])
annotation class TrimmedNotBlank(
    val message: String = "must not be blank or only whitespace",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
