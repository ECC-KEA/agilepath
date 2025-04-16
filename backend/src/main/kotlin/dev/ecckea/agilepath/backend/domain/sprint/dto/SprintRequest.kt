package dev.ecckea.agilepath.backend.domain.sprint.dto

import dev.ecckea.agilepath.backend.domain.sprint.model.Sprint
import java.time.LocalDate

data class SprintRequest(
    val id: String? = null,
    val projectId: String,
    val name: String,
    val goal: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val createdBy: String? = null,
)

fun SprintRequest.toModel() = Sprint(
    id = id ?: "",
    projectId = projectId,
    name = name,
    goal = goal,
    startDate = startDate,
    endDate = endDate,
    createdBy = createdBy ?: "",
)
