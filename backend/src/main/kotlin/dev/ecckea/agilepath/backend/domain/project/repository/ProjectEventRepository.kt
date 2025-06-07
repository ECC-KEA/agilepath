package dev.ecckea.agilepath.backend.domain.project.repository

import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEventEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProjectEventRepository : JpaRepository<ProjectEventEntity, UUID> {
    fun findOneById(id: UUID): ProjectEventEntity?
    fun findAllByProjectId(projectId: UUID): List<ProjectEventEntity>
}