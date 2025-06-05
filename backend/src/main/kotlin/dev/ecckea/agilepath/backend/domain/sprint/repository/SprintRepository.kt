package dev.ecckea.agilepath.backend.domain.sprint.repository

import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
interface SprintRepository : JpaRepository<SprintEntity, UUID> {
    fun findOneById(id: UUID): SprintEntity?
    fun findByProjectId(projectId: UUID): List<SprintEntity>

    @Query(
        value = """
            SELECT * FROM sprints 
            WHERE project_id = :projectId 
            AND start_date < :beforeDate 
            ORDER BY start_date DESC 
            LIMIT 1
        """,
        nativeQuery = true
    )
    fun findPreviousSprintInProject(
        @Param("projectId") projectId: UUID,
        @Param("beforeDate") beforeDate: LocalDate
    ): SprintEntity?

    @Query(
        value = """
            SELECT * FROM sprints 
            WHERE project_id = :projectId 
            ORDER BY start_date DESC 
            LIMIT :limit
        """,
        nativeQuery = true
    )
    fun findRecentSprintsInProject(
        @Param("projectId") projectId: UUID,
        @Param("limit") limit: Int
    ): List<SprintEntity>
}