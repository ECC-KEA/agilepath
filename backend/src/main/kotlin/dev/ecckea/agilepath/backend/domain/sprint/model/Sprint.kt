package dev.ecckea.agilepath.backend.domain.sprint.model

import dev.ecckea.agilepath.backend.shared.utils.now
import java.time.Instant
import java.time.LocalDate
import java.util.*

data class Sprint(
    val id: UUID,
    val projectId: UUID,
    val name: String,
    val goal: String?,
    val teamCapacity: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val createdBy: String, // UserId
    val createdAt: Instant = now(),
    val modifiedBy: String? = null, // UserId
    val modifiedAt: Instant? = null,
)