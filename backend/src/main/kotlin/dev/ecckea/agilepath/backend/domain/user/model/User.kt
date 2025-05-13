package dev.ecckea.agilepath.backend.domain.user.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.Instant

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
data class User(
    val id: String,
    val githubUsername: String?,
    val email: String,
    val fullName: String?,
    val avatarUrl: String?,
    val createdAt: Instant = Instant.now()
)