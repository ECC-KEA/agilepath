package dev.ecckea.agilepath.backend.domain.story.repository

import dev.ecckea.agilepath.backend.domain.story.repository.entity.SubtaskEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SubtaskRepository : JpaRepository<SubtaskEntity, UUID> {
    fun findOneById(id: UUID): SubtaskEntity?
    fun findByTaskId(taskId: UUID): List<SubtaskEntity>

    @Query("SELECT st FROM SubtaskEntity st WHERE st.task.id IN :taskIds")
    fun findByTaskIdIn(taskIds: List<UUID>): List<SubtaskEntity>
    fun countByTaskId(taskId: UUID): Int
    fun existsByIdAndTaskId(id: UUID, taskId: UUID): Boolean
}