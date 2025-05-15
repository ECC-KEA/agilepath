package dev.ecckea.agilepath.backend.shared.validation.annotations

import dev.ecckea.agilepath.backend.shared.validation.validators.RequireOneEstimateValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [RequireOneEstimateValidator::class])
annotation class RequireOneEstimate(
    val message: String = "Either T-shirt estimate or point estimate must be provided",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)