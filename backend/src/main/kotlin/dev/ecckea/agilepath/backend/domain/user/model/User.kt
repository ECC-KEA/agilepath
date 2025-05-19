package dev.ecckea.agilepath.backend.domain.user.model

import java.time.Instant

data class User(
    val id: String,
    val githubUsername: String?,
    val email: String,
    val fullName: String?,
    val avatarUrl: String?,
    val createdAt: Instant = Instant.now()
)