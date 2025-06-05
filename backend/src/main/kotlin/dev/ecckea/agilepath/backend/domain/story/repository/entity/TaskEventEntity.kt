package dev.ecckea.agilepath.backend.domain.story.repository.entity

import dev.ecckea.agilepath.backend.domain.story.model.TaskEventType
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import dev.ecckea.agilepath.backend.shared.utils.now
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "task_events")
data class TaskEventEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    val taskId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val eventType: TaskEventType,

    val oldValue: String? = null,
    val newValue: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "triggered_by", nullable = false)
    val triggeredBy: UserEntity,

    @Column(nullable = false, updatable = false)
    val createdAt: Instant = now()
)

