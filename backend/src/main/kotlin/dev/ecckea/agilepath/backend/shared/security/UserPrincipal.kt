package dev.ecckea.agilepath.backend.shared.security

import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity

/**
 * Represents the authenticated user extracted from the Clerk JWT.
 *
 * This class mirrors the identity-related claims included in the JWT,
 * and is used throughout the system for authorization, user lookup,
 * and auditing purposes.
 */
data class UserPrincipal(
    val id: String,
    val email: String,
    val githubUsername: String?,
    val fullName: String?,
    val avatarUrl: String?
)

fun UserPrincipal.toEntity() = UserEntity(
    id = id,
    githubUsername = githubUsername,
    email = email,
    fullName = fullName,
    avatarUrl = avatarUrl
)