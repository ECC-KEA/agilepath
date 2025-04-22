package dev.ecckea.agilepath.backend.domain.sprint.repository

import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SprintRepository : JpaRepository<SprintEntity, String> {
    fun findOneById(id: String): SprintEntity?
    fun findByProjectId(projectId: String): List<SprintEntity>
}