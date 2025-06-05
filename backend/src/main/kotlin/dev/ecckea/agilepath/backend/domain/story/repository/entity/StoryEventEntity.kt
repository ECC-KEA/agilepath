package dev.ecckea.agilepath.backend.domain.story.repository.entity

import dev.ecckea.agilepath.backend.domain.story.model.StoryEventType
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import dev.ecckea.agilepath.backend.shared.utils.now
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "story_events")
data class StoryEventEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    val storyId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val eventType: StoryEventType,

    val oldValue: String? = null,
    val newValue: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "triggered_by", nullable = false)
    val triggeredBy: UserEntity,

    @Column(nullable = false, updatable = false)
    val createdAt: Instant = now()
)
