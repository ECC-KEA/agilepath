package dev.ecckea.agilepath.backend.domain.user.repository.entity

import dev.ecckea.agilepath.backend.domain.user.model.User
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "users")
data class UserEntity(

    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(name = "clerk_id", nullable = false, unique = true, length = 50)
    val clerkId: String,

    @Column(name = "github_username", length = 100)
    val githubUsername: String? = null,

    @Column(name = "email", nullable = false, unique = true, length = 255)
    val email: String,

    @Column(name = "full_name", length = 255)
    val fullName: String? = null,

    @Column(name = "avatar_url", columnDefinition = "TEXT")
    val avatarUrl: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "modified_at")
    var modifiedAt: Instant? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    var modifiedBy: UserEntity? = null
)

fun UserEntity.toModel(): User = User(
    id = id,
    clerkId = clerkId,
    githubUsername = githubUsername,
    email = email,
    fullName = fullName,
    avatarUrl = avatarUrl,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
    modifiedBy = modifiedBy?.id,
)