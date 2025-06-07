package dev.ecckea.agilepath.backend.domain.story.repository

import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskEventEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TaskEventRepository : JpaRepository<TaskEventEntity, UUID> {
    fun findOneById(id: UUID): TaskEventEntity?
    fun findAllByTaskIdIn(taskIds: List<UUID>): List<TaskEventEntity>
    fun findAllByTaskId(taskId: UUID): List<TaskEventEntity>
}