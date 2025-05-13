package dev.ecckea.agilepath.backend.domain.sprint.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import dev.ecckea.agilepath.backend.shared.utils.now
import java.time.Instant
import java.time.LocalDate
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
data class Sprint(
    val id: UUID,
    val projectId: UUID,
    val name: String,
    val goal: String?,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val createdBy: String, // UserId
    val createdAt: Instant = now(),
    val modifiedBy: String? = null, // UserId
    val modifiedAt: Instant? = null,
)