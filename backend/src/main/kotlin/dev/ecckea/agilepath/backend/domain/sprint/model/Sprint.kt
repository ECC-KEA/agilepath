package dev.ecckea.agilepath.backend.domain.sprint.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.Instant
import java.time.LocalDate

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
data class Sprint(
    val id: String,
    val projectId: String,
    val name: String,
    val goal: String?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val createdBy: String, // UserId
    val createdAt: Instant = Instant.now(),
    val modifiedAt: Instant? = null,
)
