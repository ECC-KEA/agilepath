package dev.ecckea.agilepath.backend.domain.sprint.repository.entity

import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEntity
import dev.ecckea.agilepath.backend.domain.sprint.model.Sprint
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(name = "sprints")
class SprintEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    val project: ProjectEntity,

    @Column(name = "name", nullable = false, length = 255)
    val name: String,

    @Column(name = "goal", columnDefinition = "TEXT")
    val goal: String? = null,

    @Column(name = "start_date", nullable = false)
    val startDate: LocalDate,

    @Column(name = "end_date", nullable = false)
    val endDate: LocalDate,

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

fun SprintEntity.toModel(): Sprint = Sprint(
    id = id,
    projectId = project.id,
    name = name,
    goal = goal,
    startDate = startDate,
    endDate = endDate,
    createdBy = createdBy.id,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
)


fun Sprint.toEntity(
    project: ProjectEntity,
    createdBy: UserEntity,
    modifiedBy: UserEntity? = null
): SprintEntity = SprintEntity(
    id = id,
    project = project,
    name = name,
    goal = goal,
    startDate = startDate?: LocalDate.now(),
    endDate = endDate?: LocalDate.now(),
    createdBy = createdBy,
    modifiedBy = modifiedBy,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
)
