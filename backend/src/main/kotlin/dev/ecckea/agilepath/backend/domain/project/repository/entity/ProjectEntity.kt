package dev.ecckea.agilepath.backend.domain.project.repository.entity

import dev.ecckea.agilepath.backend.domain.project.model.Project
import dev.ecckea.agilepath.backend.domain.project.type.Framework
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "projects")
class ProjectEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String,

    @Column(name = "name", nullable = false, unique = true, length = 100)
    val name: String,

    @Column(name = "description", columnDefinition = "TEXT")
    val description: String? = null,

    @Column(name = "framework", nullable = false, length = 50)
    val framework: Framework,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    val createdBy: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    val modifiedBy: UserEntity? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "modified_at")
    val modifiedAt: Instant? = null,
)

fun ProjectEntity.toModel(): Project = Project(
    id = id,
    name = name,
    description = description,
    framework = framework,
    createdBy = createdBy.id,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
)

fun Project.toEntity(
    createdBy: UserEntity,
    modifiedBy: UserEntity? = null
): ProjectEntity = ProjectEntity(
    id = id,
    name = name,
    description = description,
    framework = framework,
    createdBy = createdBy,
    modifiedBy = modifiedBy,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
)



