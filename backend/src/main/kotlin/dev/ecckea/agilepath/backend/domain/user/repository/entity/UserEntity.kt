package dev.ecckea.agilepath.backend.domain.user.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "users")
class UserEntity(

    @Id
    val id: String,

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
)