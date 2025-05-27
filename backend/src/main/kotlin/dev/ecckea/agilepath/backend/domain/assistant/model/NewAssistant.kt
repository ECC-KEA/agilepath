package dev.ecckea.agilepath.backend.domain.assistant.model

data class NewAssistant(
    val name: String,
    val description: String? = null,
    val model: String,
    val prompt: String? = null,
    val temperature: Double? = 0.7,
    val topP: Double? = 1.0,
    val maxTokens: Int? = 1000,
)
