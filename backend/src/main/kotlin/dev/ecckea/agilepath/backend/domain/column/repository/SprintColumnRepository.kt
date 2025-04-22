package dev.ecckea.agilepath.backend.domain.column.repository

import dev.ecckea.agilepath.backend.domain.column.repository.entity.SprintColumnEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SprintColumnRepository : JpaRepository<SprintColumnEntity, UUID> {
    fun findOneById(id: UUID): SprintColumnEntity?
    fun findBySprintId(sprintId: UUID): List<SprintColumnEntity>
    fun existsByNameAndSprintId(name: String, sprintId: UUID): Boolean
    fun findByNameAndSprintId(name: String, sprintId: UUID): SprintColumnEntity?
}