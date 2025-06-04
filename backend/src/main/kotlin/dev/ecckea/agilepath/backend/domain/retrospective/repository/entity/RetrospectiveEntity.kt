package dev.ecckea.agilepath.backend.domain.retrospective.repository.entity

import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEntity
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "retrospectives")
class RetrospectiveEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,

    @OneToOne
    @JoinColumn(name = "sprint_id", nullable = false)
    val sprint: SprintEntity,

    val completedAt: Instant? = null,

    @ElementCollection
    @CollectionTable(name = "retrospective_talking_points", joinColumns = [JoinColumn(name = "retrospective_id")])
    @Column(name = "talking_point")
    val talkingPoints: List<TalkingPointEmbeddable> = emptyList(),

    val teamMood: String? = null,

    @ElementCollection
    @CollectionTable(name = "retrospective_keep_doing", joinColumns = [JoinColumn(name = "retrospective_id")])
    @Column(name = "keep_doing")
    val keepDoing: List<String> = emptyList(),

    @ElementCollection
    @CollectionTable(name = "retrospective_stop_doing", joinColumns = [JoinColumn(name = "retrospective_id")])
    @Column(name = "stop_doing")
    val stopDoing: List<String> = emptyList(),

    @ElementCollection
    @CollectionTable(name = "retrospective_start_doing", joinColumns = [JoinColumn(name = "retrospective_id")])
    @Column(name = "start_doing")
    val startDoing: List<String> = emptyList(),
)