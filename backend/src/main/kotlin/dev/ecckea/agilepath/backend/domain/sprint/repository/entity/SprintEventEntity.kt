package dev.ecckea.agilepath.backend.domain.sprint.repository.entity

import dev.ecckea.agilepath.backend.domain.sprint.model.SprintEventType
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import dev.ecckea.agilepath.backend.shared.utils.now
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "sprint_events")
data class SprintEventEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    val sprintId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val eventType: SprintEventType,

    val oldValue: String? = null,
    val newValue: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "triggered_by", nullable = false)
    val triggeredBy: UserEntity,

    @Column(nullable = false, updatable = false)
    val createdAt: Instant = now()
)