package dev.ecckea.agilepath.backend.domain.user.service

import dev.ecckea.agilepath.backend.domain.user.model.User
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

    fun get(principal: UserPrincipal): User {
        return getById(principal.id)
    }

    fun getById(id: String): User {
        // Check if the user is in the cache
        cacheService.getUser(id)?.let { return it }
        // If not, fetch from the database and cache it
        return getFromDbAndCache(id)
    }

    private fun getFromDbAndCache(id: String): User {
        log.debug("Fetching user $id from database")
        val user = ctx.user.findOneById(id)?.toModel()
            ?: throw ResourceNotFoundException("User with id $id not found")

        cacheService.cacheUser(user)
        return user
    }
}