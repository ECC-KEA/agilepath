package dev.ecckea.agilepath.backend.domain.story.repository.entity

import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEntity
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "stories")
data class StoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    val project: ProjectEntity,

    @Column(nullable = false)
    val title: String,

    val description: String? = null,

    @Column(nullable = false)
    val status: String,

    val priority: Int = 0,

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