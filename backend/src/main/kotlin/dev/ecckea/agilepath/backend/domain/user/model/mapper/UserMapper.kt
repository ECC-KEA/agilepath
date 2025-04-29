package dev.ecckea.agilepath.backend.domain.user.model.mapper

import dev.ecckea.agilepath.backend.domain.user.dto.UserResponse
import dev.ecckea.agilepath.backend.domain.user.model.User
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import dev.ecckea.agilepath.backend.shared.utils.toZonedDateTime

// Entity to Model
fun UserEntity.toModel(): User = User(
    id = id,
    githubUsername = githubUsername,
    email = email,
    fullName = fullName,
    avatarUrl = avatarUrl,
    createdAt = createdAt
)

// Model to Response DTO
fun User.toDTO(): UserResponse = UserResponse(
    id = id,
    email = email,
    fullName = fullName,
    avatarUrl = avatarUrl,
    githubUsername = githubUsername,
    githubProfileUrl = githubUsername?.let { "https://github.com/$it" },
    createdAt = toZonedDateTime(createdAt)
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    githubUsername = githubUsername,
    email = email,
    fullName = fullName,
    avatarUrl = avatarUrl,
    createdAt = createdAt
)