package dev.ecckea.agilepath.backend.shared.dto

import java.time.ZonedDateTime

data class ErrorResponse(
    val status: Int,
    val message: String,
    val timestamp: ZonedDateTime
)
