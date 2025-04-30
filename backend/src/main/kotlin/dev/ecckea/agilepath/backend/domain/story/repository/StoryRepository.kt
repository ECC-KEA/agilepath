package dev.ecckea.agilepath.backend.domain.story.repository

import dev.ecckea.agilepath.backend.domain.story.repository.entity.StoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StoryRepository : JpaRepository<StoryEntity, UUID> {
    fun findOneById(id: UUID): StoryEntity?
    fun findAllByProjectId(projectId: UUID): List<StoryEntity>
}