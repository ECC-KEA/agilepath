package dev.ecckea.agilepath.backend.domain.user.service

import dev.ecckea.agilepath.backend.domain.user.model.User
import dev.ecckea.agilepath.backend.domain.user.repository.UserRepository
import dev.ecckea.agilepath.backend.domain.user.repository.entity.toModel
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.logging.Logged
import dev.ecckea.agilepath.backend.shared.security.UserPrincipal
import dev.ecckea.agilepath.backend.shared.security.toEntity
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) : Logged() {
    /**
     * Returns the user matching the id of the principal. If the user does not exist, it will be created.
     * Caches the result using the id as key.
     *
     * @param principal the principal to get the user for
     * @return the user matching the id of the principal
     * @throws ResourceNotFoundException if the user does not exist
     */
    @Cacheable("users", key = "#principal.id")
    fun getOrCreate(principal: UserPrincipal): User {
        val exists = userRepository.existsById(principal.id)
        if (!exists) {
            log.info("User with id ${principal.id} does not exist, creating it")
            return userRepository.save(principal.toEntity()).toModel()
        }
        return userRepository.findOneById(principal.id)?.toModel()
            ?: throw ResourceNotFoundException("User with id ${principal.id} not found")
    }
}