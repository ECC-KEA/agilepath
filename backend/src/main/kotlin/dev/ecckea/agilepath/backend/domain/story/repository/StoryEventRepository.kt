package dev.ecckea.agilepath.backend.domain.story.repository

import dev.ecckea.agilepath.backend.domain.story.repository.entity.StoryEventEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface StoryEventRepository : JpaRepository<StoryEventEntity, UUID> {
    fun findOneById(id: UUID): StoryEventEntity?
}