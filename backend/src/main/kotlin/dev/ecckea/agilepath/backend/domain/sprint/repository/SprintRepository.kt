package dev.ecckea.agilepath.backend.domain.sprint.repository

import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEntity
import org.springframework.stereotype.Repository

@Repository
interface SprintRepository {
    fun findOneById(id: String): SprintEntity?
}