package dev.ecckea.agilepath.backend.domain.assistant.repository

import dev.ecckea.agilepath.backend.domain.assistant.repository.entity.AssistantEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AssistantRepository : JpaRepository<AssistantEntity, UUID> {
    fun findByName(name: String): AssistantEntity?
    fun existsByName(name: String): Boolean
}