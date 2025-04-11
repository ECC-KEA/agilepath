package dev.ecckea.agilepath.backend.domain.user.dto

import java.time.ZonedDateTime
import java.util.*
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer

data class UserResponse(
    val id: String,
    val email: String,
    val fullName: String?,
    val avatarUrl: String?,
    val githubUsername: String?,
    val githubProfileUrl: String?,
    @JsonSerialize(using = ZonedDateTimeSerializer::class)
    val createdAt: ZonedDateTime
)
