package dev.ecckea.agilepath.backend.domain.project.repository

import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEntity
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProjectRepository : JpaRepository<ProjectEntity, UUID> {
    fun findOneById(id: UUID): ProjectEntity?
    fun findByName(name: String): ProjectEntity?
    fun existsByName(name: String): Boolean
    fun findAllByCreatedBy(user: UserEntity): List<ProjectEntity>

    @Query(
        value = """
            SELECT p.* FROM projects p 
            JOIN sprints s ON s.project_id = p.id 
            WHERE s.id = :sprintId
        """,
        nativeQuery = true
    )
    fun findBySprintId(@Param("sprintId") sprintId: UUID): ProjectEntity?
}
