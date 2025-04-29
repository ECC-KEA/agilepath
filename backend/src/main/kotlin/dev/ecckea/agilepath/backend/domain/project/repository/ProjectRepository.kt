package dev.ecckea.agilepath.backend.domain.project.repository

import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEntity
import dev.ecckea.agilepath.backend.domain.user.model.User
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import dev.ecckea.agilepath.backend.shared.security.UserPrincipal
import dev.ecckea.agilepath.backend.shared.security.toEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProjectRepository : JpaRepository<ProjectEntity, UUID> {
    fun findOneById(id: UUID): ProjectEntity?
    fun findByName(name: String): ProjectEntity?
    fun existsByName(name: String): Boolean
    fun findAllByCreatedBy(user: UserEntity): List<ProjectEntity>
}
