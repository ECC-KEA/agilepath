package dev.ecckea.agilepath.backend.domain.user.dto

import java.time.ZonedDateTime
import java.util.*

data class UserResponse(
    val id: UUID,
    val email: String,
    val fullName: String?,
    val avatarUrl: String?,
    val githubUsername: String?,
    val githubProfileUrl: String?,
    val createdAt: ZonedDateTime
)
