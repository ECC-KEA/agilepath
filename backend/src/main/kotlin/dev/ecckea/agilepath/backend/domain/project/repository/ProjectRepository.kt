package dev.ecckea.agilepath.backend.domain.project.repository

import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepository : JpaRepository<ProjectEntity, String> {
    fun findOneById(id: String): ProjectEntity?
    fun findByName(name: String): ProjectEntity?
    fun existsByName(name: String): Boolean
    fun findAllByUserId(userId: String): List<ProjectEntity>
}