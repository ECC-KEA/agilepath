package dev.ecckea.agilepath.backend.domain.story.repository.entity

import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.Check
import java.time.Instant
import java.util.*

@Entity
@Table(name = "comments")
@Check(
    constraints = """
    (story_id IS NOT NULL AND task_id IS NULL) OR
    (story_id IS NULL AND task_id IS NOT NULL)
"""
)
data class CommentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    val content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    val story: StoryEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    val task: TaskEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    val createdBy: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    val modifiedBy: UserEntity? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "modified_at")
    val modifiedAt: Instant? = null
)