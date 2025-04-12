package dev.ecckea.agilepath.backend.domain.user.application

import dev.ecckea.agilepath.backend.domain.user.model.User
import dev.ecckea.agilepath.backend.domain.user.service.UserService
import dev.ecckea.agilepath.backend.shared.security.UserPrincipal
import org.springframework.stereotype.Service

@Service
class UserAuthApplication (
    private val userService: UserService,
) {
    suspend fun getCurrentUser(principal: UserPrincipal): User {
        return userService.getOrCreate(principal)
    }
}