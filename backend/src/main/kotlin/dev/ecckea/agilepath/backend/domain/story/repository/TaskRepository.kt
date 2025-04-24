package dev.ecckea.agilepath.backend.domain.story.repository

import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TaskRepository : JpaRepository<TaskEntity, UUID> {
}