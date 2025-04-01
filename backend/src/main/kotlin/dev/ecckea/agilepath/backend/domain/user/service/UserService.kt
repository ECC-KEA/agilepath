package dev.ecckea.agilepath.backend.domain.user.service

import dev.ecckea.agilepath.backend.domain.user.repository.entity.toModel
import dev.ecckea.agilepath.backend.domain.user.model.User
import dev.ecckea.agilepath.backend.domain.user.repository.UserRepository
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import dev.ecckea.agilepath.backend.shared.security.UserPrincipal
import dev.ecckea.agilepath.backend.shared.security.toEntity
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun getOrCreate(principal: UserPrincipal): User {
        val exists = userRepository.existsByClerkId(principal.clerkId)
        if (!exists) {
            userRepository.save(principal.toEntity())
        }
        val user = userRepository.findByClerkId(principal.clerkId) ?: throw ResourceNotFoundException("User not found")
        return user.toModel()
    }
}