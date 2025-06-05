package dev.ecckea.agilepath.backend.domain.user.service

import dev.ecckea.agilepath.backend.domain.user.model.User
import dev.ecckea.agilepath.backend.domain.user.model.mapper.toEntity
import dev.ecckea.agilepath.backend.domain.user.model.mapper.toModel
import dev.ecckea.agilepath.backend.infrastructure.cache.CacheService
import dev.ecckea.agilepath.backend.infrastructure.cache.cacheUser
import dev.ecckea.agilepath.backend.infrastructure.cache.getUser
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.security.UserPrincipal
import dev.ecckea.agilepath.backend.shared.security.toEntity
import org.springframework.stereotype.Service

@Service
class UserService(
    private val ctx: RepositoryContext,
    private val cacheService: CacheService
) : Logged() {


    fun getOrCreate(principal: UserPrincipal): User {
        // Check if the user is in the cache
        cacheService.getUser(principal.id)?.let { return it }

        // If not, check if the user exists in the database
        val exists = ctx.user.existsById(principal.id)

        // If the user does not exist, create it and cache it
        if (!exists) {
            log.info("User with id ${principal.id} does not exist, creating it")
            val savedUser = ctx.user.save(principal.toEntity()).toModel()
            cacheService.cacheUser(savedUser)
            return savedUser
        }
        // If the user exists, fetch it from the database and cache it
        return getFromDbAndCache(principal.id)
    }

    fun getOrCreateSystemUser(): User {
        val systemUserId = "system"
        val systemUser = User(
            id = systemUserId,
            githubUsername = "system",
            email = "systemUser@AgilePath.dk",
            fullName = "System User",
            avatarUrl = "https://avatars.githubusercontent.com/u/90181953?s=96&v=4"
        )

        // Check if the system user is in the cache
        cacheService.getUser(systemUserId)?.let { return it }

        // If not, check if the system user exists in the database
        val exists = ctx.user.existsById(systemUserId)

        // If the system user does not exist, create it and cache it
        if (!exists) {
            log.info("System user does not exist, creating it")

            val savedUser = ctx.user.save(systemUser.toEntity()).toModel()
            cacheService.cacheUser(savedUser)
            return savedUser
        }
        // If the system user exists, fetch it from the database and cache it
        return getFromDbAndCache(systemUserId)
    }


    fun get(principal: UserPrincipal): User {
        return getById(principal.id)
    }

    fun getById(id: String): User {
        // Check if the user is in the cache
        cacheService.getUser(id)?.let { return it }
        // If not, fetch from the database and cache it
        return getFromDbAndCache(id)
    }

    fun getBySearch(
        fullname: String,
        email: String,
        githubUsername: String
    ): List<User> {
        return ctx.user.findAllByFullNameContainsIgnoreCaseOrEmailContainsIgnoreCaseOrGithubUsernameContainsIgnoreCase(
            fullname,
            email,
            githubUsername
        ).map { it.toModel() }
    }

    private fun getFromDbAndCache(id: String): User {
        log.debug("Fetching user $id from database")
        val user = ctx.user.findOneById(id)?.toModel()
            ?: throw ResourceNotFoundException("User with id $id not found")

        cacheService.cacheUser(user)
        return user
    }
}
