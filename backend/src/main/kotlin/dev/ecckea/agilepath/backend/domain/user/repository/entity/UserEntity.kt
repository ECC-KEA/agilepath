package dev.ecckea.agilepath.backend.domain.user.repository.entity

import dev.ecckea.agilepath.backend.domain.user.model.User
import dev.ecckea.agilepath.backend.shared.utils.nowInZone
import jakarta.persistence.*
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer

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

    // @JsonSerialize(using = InstantSerializer::class)
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    // @JsonSerialize(using = InstantSerializer::class)
    @Column(name = "modified_at")
    var modifiedAt: Instant? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    var modifiedBy: UserEntity? = null
)

fun UserEntity.toModel(): User = User(
    id = id,
    githubUsername = githubUsername,
    email = email,
    fullName = fullName,
    avatarUrl = avatarUrl,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
    modifiedBy = modifiedBy?.id,
)