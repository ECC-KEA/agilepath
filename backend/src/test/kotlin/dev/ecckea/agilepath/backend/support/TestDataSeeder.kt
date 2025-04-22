package dev.ecckea.agilepath.backend.support

import dev.ecckea.agilepath.backend.domain.user.repository.UserRepository
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TestDataSeeder(
    private val userRepository: UserRepository
) {

    val testUserId = "user-id"

    @Transactional
    fun ensureTestUserExists(): UserEntity {
        return userRepository.findById(testUserId).orElseGet {
            userRepository.save(
                UserEntity(
                    id = testUserId,
                    email = "test@example.com",
                    githubUsername = "testuser",
                    fullName = "Test User",
                    avatarUrl = "http://test"
                )
            )
        }
    }
}
