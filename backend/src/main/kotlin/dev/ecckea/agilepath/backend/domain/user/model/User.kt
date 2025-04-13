package dev.ecckea.agilepath.backend.domain.user.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import dev.ecckea.agilepath.backend.domain.user.dto.UserResponse
import dev.ecckea.agilepath.backend.shared.utils.toZonedDateTime
import java.time.Instant

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonTypeName("dev.ecckea.agilepath.backend.domain.user.model.User")
data class User(
    val id: String,
    val githubUsername: String?,
    val email: String,
    val fullName: String?,
    val avatarUrl: String?,
    val createdAt: Instant = Instant.now(),
    val modifiedAt: Instant? = null,
    val modifiedBy: String? // UserId
)

fun User.toDTO(): UserResponse = UserResponse(
    id = id,
    email = email,
    fullName = fullName,
    avatarUrl = avatarUrl,
    githubUsername = githubUsername,
    githubProfileUrl = githubUsername?.let { "https://github.com/$it" },
    createdAt = toZonedDateTime(createdAt)
)