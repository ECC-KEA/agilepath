package dev.ecckea.agilepath.backend.shared.validation.annotations

import dev.ecckea.agilepath.backend.shared.validation.validators.RequireOneEstimateValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [RequireOneEstimateValidator::class])
annotation class RequireOneEstimate(
    val message: String = "Exactly one estimate type (T-shirt OR point estimate) must be provided, not both",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)