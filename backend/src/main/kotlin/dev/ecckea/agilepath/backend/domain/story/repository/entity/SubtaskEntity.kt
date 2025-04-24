package dev.ecckea.agilepath.backend.domain.subtask.repository.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
data class SubtaskEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    val task: TaskEntity,

    @Column(nullable = false)
    val title: String,

    val description: String? = null,

    @Column(name = "is_done", nullable = false)
    val isDone: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    val createdBy: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    val modifiedBy: UserEntity? = null,

    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),

    val modifiedAt: Instant? = null
)