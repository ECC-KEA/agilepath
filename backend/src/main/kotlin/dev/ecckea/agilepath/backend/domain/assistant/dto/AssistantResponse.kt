package dev.ecckea.agilepath.backend.domain.assistant.dto

data class AssistantResponse(
    val id: String,
    val name: String,
    val description: String?,
    val model: String?,
    val prompt: String?,
    val temperature: Double?,
    val topP: Double?,
    val maxTokens: Int?,
)
