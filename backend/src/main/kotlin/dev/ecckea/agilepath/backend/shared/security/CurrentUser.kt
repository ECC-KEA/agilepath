package dev.ecckea.agilepath.backend.shared.security

import org.springframework.security.core.context.SecurityContextHolder

fun currentUser(): UserPrincipal {
    val auth = SecurityContextHolder.getContext().authentication
        ?: throw IllegalStateException("No authentication in security context")

    return auth.principal as? UserPrincipal
        ?: throw IllegalStateException("Authenticated principal is not a UserPrincipal")
}