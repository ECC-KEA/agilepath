package dev.ecckea.agilepath.backend.domain.retrospective.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class TalkingPointEmbeddable(
    @Column(name = "prompt")
    val prompt: String,

    @Column(name = "response")
    val response: String? = null
)
