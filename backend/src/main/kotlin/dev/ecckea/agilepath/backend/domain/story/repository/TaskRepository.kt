package dev.ecckea.agilepath.backend.domain.story.repository

import dev.ecckea.agilepath.backend.domain.column.repository.entity.SprintColumnEntity
import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TaskRepository : JpaRepository<TaskEntity, UUID> {
    fun findByStoryId(storyId: UUID): List<TaskEntity>
    fun findOneById(id: UUID): TaskEntity?
    fun findBySprintColumn(sprintColumn: SprintColumnEntity): List<TaskEntity>

    @Query(
        value = """
        SELECT * FROM tasks t
        JOIN sprint_columns sc ON t.sprint_column_id = sc.id
        WHERE sc.sprint_id = :sprintId AND sc.status = 'DONE'
    """,
        nativeQuery = true
    )
    fun findCompletedTasksForSprint(@Param("sprintId") sprintId: UUID): List<TaskEntity>

    @Query(
        value = """
        SELECT COUNT(*) FROM tasks t
        JOIN sprint_columns sc ON t.sprint_column_id = sc.id
        WHERE sc.sprint_id = :sprintId AND sc.status = 'DONE'
    """,
        nativeQuery = true
    )
    fun countCompletedTasksForSprint(@Param("sprintId") sprintId: UUID): Int

    @Query(
        value = """
        SELECT t.* FROM tasks t
        JOIN sprint_columns sc ON t.sprint_column_id = sc.id
        WHERE sc.sprint_id = :sprintId
    """,
        nativeQuery = true
    )
    fun findAllTasksForSprint(@Param("sprintId") sprintId: UUID): List<TaskEntity>

    @Query(
        value = """
        SELECT COUNT(t.*) FROM tasks t
        JOIN sprint_columns sc ON t.sprint_column_id = sc.id
        WHERE sc.sprint_id = :sprintId
    """,
        nativeQuery = true
    )
    fun countAllTasksForSprint(@Param("sprintId") sprintId: UUID): Int
}