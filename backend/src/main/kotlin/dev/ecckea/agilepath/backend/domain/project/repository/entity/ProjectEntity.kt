package dev.ecckea.agilepath.backend.domain.project.repository.entity

import dev.ecckea.agilepath.backend.domain.project.model.Framework
import dev.ecckea.agilepath.backend.domain.project.model.NewProject
import dev.ecckea.agilepath.backend.domain.project.model.Project
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

    @Version
    @Column(name = "version", nullable = false)
    var version: Long = 0,
)

fun ProjectEntity.toModel(): Project {
    return id?.let {
        Project(
            id = it,
            name = name,
            description = description,
            framework = framework,
            createdBy = createdBy.id,
            createdAt = createdAt,
            modifiedBy = modifiedBy?.id,
            modifiedAt = modifiedAt,
        )
    } ?: throw IllegalStateException("Cannot convert ProjectEntity to Project model without an ID")
}

fun ProjectEntity.updatedWith(update: NewProject, modifiedBy: UserEntity): ProjectEntity =
    ProjectEntity(
        id = this.id,
        name = update.name,
        description = update.description,
        framework = update.framework,
        createdBy = this.createdBy,
        modifiedBy = modifiedBy,
        createdAt = this.createdAt,
        modifiedAt = now(),
        version = this.version,
    )