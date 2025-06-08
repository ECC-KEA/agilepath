package dev.ecckea.agilepath.backend.domain.retrospective.repository

import dev.ecckea.agilepath.backend.domain.retrospective.repository.entity.RetrospectiveEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RetrospectiveRepository : JpaRepository<RetrospectiveEntity, UUID> {
    fun findBySprintId(sprintId: UUID): RetrospectiveEntity?

}