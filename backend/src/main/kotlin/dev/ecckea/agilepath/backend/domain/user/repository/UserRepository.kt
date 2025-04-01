package dev.ecckea.agilepath.backend.domain.user.repository

import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<UserEntity, UUID> {

    fun findByEmail(email: String): UserEntity?

    fun findByClerkId(clerkId: String): UserEntity?

    fun findByGithubUsername(githubUsername: String): UserEntity?

    fun existsByEmail(email: String): Boolean

    fun existsByClerkId(clerkId: String): Boolean

    fun existsByGithubUsername(githubUsername: String): Boolean
}