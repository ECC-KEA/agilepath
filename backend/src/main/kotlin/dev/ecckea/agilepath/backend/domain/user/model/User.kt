package dev.ecckea.agilepath.backend.domain.user.model

import dev.ecckea.agilepath.backend.domain.user.dto.UserResponse
import dev.ecckea.agilepath.backend.shared.utils.toZonedDateTime
import java.time.Instant
import java.util.*
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer

data class User(
    val id: String,
    val githubUsername: String?,
    val email: String,
    val fullName: String?,
    val avatarUrl: String?,
    @JsonSerialize(using = InstantSerializer::class)
    val createdAt: Instant = Instant.now(),
    @JsonSerialize(using = InstantSerializer::class)
    val modifiedAt: Instant? = null,
    val modifiedBy: String? // UserId
) {
    constructor() : this("", null, "", null, null, Instant.now(), null, null) {
        // Default constructor for JPA
    }
    
}

fun User.toDTO(): UserResponse = UserResponse(
    id = id,
    email = email,
    fullName = fullName,
    avatarUrl = avatarUrl,
    githubUsername = githubUsername,
    githubProfileUrl = githubUsername?.let { "https://github.com/$it" },
    createdAt = toZonedDateTime(createdAt)
)