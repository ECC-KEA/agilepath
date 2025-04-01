package dev.ecckea.agilepath.backend.shared.dto

import java.time.Instant

data class ErrorResponse(
    val status: Int,
    val message: String,
    val timestamp: Instant
)
