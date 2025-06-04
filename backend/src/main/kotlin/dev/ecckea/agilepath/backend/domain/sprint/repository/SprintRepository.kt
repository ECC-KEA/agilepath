package dev.ecckea.agilepath.backend.domain.sprint.repository

import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SprintRepository : JpaRepository<SprintEntity, UUID> {
    fun findOneById(id: UUID): SprintEntity?
    fun findByProjectId(projectId: UUID): List<SprintEntity>

}