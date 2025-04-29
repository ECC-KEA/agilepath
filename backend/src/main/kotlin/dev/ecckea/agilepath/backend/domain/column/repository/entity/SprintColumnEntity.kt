package dev.ecckea.agilepath.backend.domain.column.repository.entity

import dev.ecckea.agilepath.backend.domain.column.model.SprintColumnStatus
import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEntity
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "sprint_columns")
class SprintColumnEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_id", nullable = false)
    val sprint: SprintEntity,

    @Column(name = "name", nullable = false, length = 255)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    val status: SprintColumnStatus,

    @Column(name = "column_index", nullable = false)
    val columnIndex: Int? = null,
)