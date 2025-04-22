package dev.ecckea.agilepath.backend.domain.project.repository

import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProjectRepository : JpaRepository<ProjectEntity, UUID> {
    fun findOneById(id: UUID): ProjectEntity?
    fun findByName(name: String): ProjectEntity?
    fun existsByName(name: String): Boolean
}
