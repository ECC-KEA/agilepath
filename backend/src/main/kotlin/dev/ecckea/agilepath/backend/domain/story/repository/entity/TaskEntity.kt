package dev.ecckea.agilepath.backend.domain.story.repository.entity

import dev.ecckea.agilepath.backend.domain.column.repository.entity.SprintColumnEntity
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
data class TaskEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id", nullable = false)
    val story: StoryEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_column_id", nullable = false)
    val sprintColumn: SprintColumnEntity,

    @Column(nullable = false)
    val title: String,

    val description: String? = null,

    val estimateTshirt: String? = null,

    val estimatePoints: Int? = null,

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