package dev.ecckea.agilepath.backend.domain.project.repository.entity

import dev.ecckea.agilepath.backend.domain.project.model.EstimationMethod
import dev.ecckea.agilepath.backend.domain.project.model.Framework
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import dev.ecckea.agilepath.backend.shared.utils.now
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "projects")
class ProjectEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "name", nullable = false, unique = true, length = 100)
    val name: String,

    @Column(name = "description", columnDefinition = "TEXT")
    val description: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "framework", nullable = false, length = 50)
    val framework: Framework,

    @Enumerated(EnumType.STRING)
    @Column(name = "estimation_method", nullable = false, length = 50)
    val estimationMethod: EstimationMethod,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    val createdBy: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    val modifiedBy: UserEntity? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = now(),

    @Column(name = "modified_at")
    val modifiedAt: Instant? = null,
)