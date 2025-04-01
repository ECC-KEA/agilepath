package dev.ecckea.agilepath.backend.shared.security

import org.springframework.security.core.context.SecurityContextHolder

fun currentUser(): UserPrincipal =
    SecurityContextHolder.getContext().authentication.principal as UserPrincipal