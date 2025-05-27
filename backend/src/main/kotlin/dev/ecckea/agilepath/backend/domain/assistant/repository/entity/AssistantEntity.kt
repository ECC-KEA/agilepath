package dev.ecckea.agilepath.backend.domain.assistant.repository.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "llm_assistants")
class AssistantEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "name", nullable = false, length = 255)
    val name: String,

    @Column(name = "description", columnDefinition = "TEXT")
    val description: String? = null,

    @Column(name = "model", nullable = false, length = 50)
    val model: String,

    @Column(name = "prompt", columnDefinition = "TEXT")
    val prompt: String? = null,

    @Column(name = "temperature", nullable = false)
    val temperature: Double = 0.7,

    @Column(name = "top_p", nullable = false)
    val topP: Double = 1.0,

    @Column(name = "max_tokens", nullable = false)
    val maxTokens: Int = 1000,
)