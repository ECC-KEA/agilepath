package dev.ecckea.agilepath.backend.domain.user.application

import dev.ecckea.agilepath.backend.domain.user.model.User
import dev.ecckea.agilepath.backend.domain.user.service.UserService
import dev.ecckea.agilepath.backend.shared.security.UserPrincipal
import org.springframework.stereotype.Service

@Service
class UserApplication(
    private val userService: UserService,
) {
    fun getCurrentUser(principal: UserPrincipal): User {
        return userService.getOrCreate(principal)
    }

    fun getUserById(id: String): User {
        return userService.getById(id)
    }

    fun getUsersBySearch(
        search: String,
    ): List<User> {
        return userService.getBySearch(search, search, search)
    }
}
