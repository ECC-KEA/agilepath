package dev.ecckea.agilepath.backend.domain.column.repository.entity

import dev.ecckea.agilepath.backend.domain.column.type.SprintColumnStatus
import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEntity
import jakarta.persistence.*
import dev.ecckea.agilepath.backend.domain.column.model.SprintColumn

@Entity
@Table(name = "columns")
class SprintColumnEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id", nullable = false)
    val sprint: SprintEntity,

    @Column(name = "name", nullable = false, length = 255)
    val name: String,

    @Column(name = "status", nullable = false, length = 50)
    val status: SprintColumnStatus,

    @Column(name = "column_index", nullable = false)
    val columnIndex: Int,
)

fun SprintColumnEntity.toModel(): SprintColumn = SprintColumn(
    id = id,
    sprintId = sprint.id,
    name = name,
    status = status,
    columnIndex = columnIndex
)

fun SprintColumn.toEntity(sprintColumn: SprintColumn, sprint: SprintEntity): SprintColumnEntity = SprintColumnEntity(
    id = sprintColumn.id,
    sprint = sprint,
    name = sprintColumn.name,
    status = SprintColumnStatus.valueOf(sprintColumn.status.toString()),
    columnIndex = sprintColumn.columnIndex
)