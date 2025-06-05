package dev.ecckea.agilepath.backend.domain.sprint.repository

import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEventEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SprintEventRepository : JpaRepository<SprintEventEntity, UUID> {
    fun findOneById(id: UUID): SprintEventEntity?
    fun findAllBySprintId(sprintId: UUID): List<SprintEventEntity>
}