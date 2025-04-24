package dev.ecckea.agilepath.backend.domain.story.repository.entity

import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import jakarta.persistence.*
import java.util.*

@Entity
@IdClass(TaskAssigneeId::class)
data class TaskAssigneeEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    val task: TaskEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity
)

data class TaskAssigneeId(
    val task: UUID = UUID.randomUUID(),
    val user: String = ""
) : java.io.Serializable
