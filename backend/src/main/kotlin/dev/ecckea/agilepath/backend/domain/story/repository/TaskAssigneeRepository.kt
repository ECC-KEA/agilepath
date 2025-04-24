package dev.ecckea.agilepath.backend.domain.story.repository

import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskAssigneeEntity
import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskAssigneeId
import org.springframework.data.jpa.repository.JpaRepository

interface TaskAssigneeRepository : JpaRepository<TaskAssigneeEntity, TaskAssigneeId> {

}
