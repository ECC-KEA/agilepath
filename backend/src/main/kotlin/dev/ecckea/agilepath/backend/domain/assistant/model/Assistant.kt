package dev.ecckea.agilepath.backend.domain.assistant.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.util.UUID

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
data class Assistant(
    val id: UUID,
    val name: String,
    val description: String,
    val model: String,
    val prompt: String,
    val temperature: Double,
    val topP: Double,
    val maxTokens: Int,
)
