package dev.ecckea.agilepath.backend.domain.user.repository

import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<UserEntity, String> {

    fun findOneById(id: String): UserEntity?

    fun findByEmail(email: String): UserEntity?

    fun findByGithubUsername(githubUsername: String): UserEntity?

    fun existsByEmail(email: String): Boolean

    fun existsByGithubUsername(githubUsername: String): Boolean

    fun findAllByFullNameContainsIgnoreCaseOrEmailContainsIgnoreCaseOrGithubUsernameContainsIgnoreCase(
        fullname: String,
        email: String,
        githubUsername: String
    ): List<UserEntity>

}