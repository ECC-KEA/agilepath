package dev.ecckea.agilepath.backend.domain.retrospective.repository.entity

import jakarta.persistence.Embeddable

@Embeddable
data class TalkingPointEmbeddable(
    val prompt: String,
    val response: String? = null
)
