package dev.ecckea.agilepath.backend.domain.project.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import java.time.Instant
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonTypeName("dev.ecckea.agilepath.backend.domain.project.model.Project")
data class Project(
    val id: UUID,
    val name: String,
    val description: String?,
    val framework: Framework,
    val createdBy: String, // UserId
    val createdAt: Instant,
    val modifiedBy: String? = null, // UserId
    val modifiedAt: Instant? = null,
    )
