package dev.ecckea.agilepath.backend.domain.project.repository.entity

import dev.ecckea.agilepath.backend.domain.project.model.ProjectRole
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "users_projects")
@IdClass(UserProjectId::class)
data class UserProjectEntity(

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    val project: ProjectEntity,

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    val role: ProjectRole
)

data class UserProjectId(
    val user: String = "",
    val project: UUID = UUID.randomUUID()
) : java.io.Serializable