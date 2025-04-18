package dev.ecckea.agilepath.backend.domain.column.repository

import dev.ecckea.agilepath.backend.domain.column.repository.entity.SprintColumnEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SprintColumnRepository : JpaRepository<SprintColumnEntity, String> {
    fun findOneById(id: String): SprintColumnEntity?
    fun findBySprintId(sprintId: String): List<SprintColumnEntity>
    fun existsByNameAndSprintId(name: String, sprintId: String): Boolean
    fun findByNameAndSprintId(name: String, sprintId: String): SprintColumnEntity?
}