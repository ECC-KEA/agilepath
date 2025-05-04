package dev.ecckea.agilepath.backend.shared.validation.annotations


import dev.ecckea.agilepath.backend.shared.validation.validators.NoHtmlValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [NoHtmlValidator::class])
annotation class NoHtml(
    val message: String = "must not contain HTML",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
