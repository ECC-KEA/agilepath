package dev.ecckea.agilepath.backend.domain.story.repository

import dev.ecckea.agilepath.backend.domain.story.repository.entity.CommentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CommentRepository : JpaRepository<CommentEntity, UUID> {
    fun findOneById(id: UUID): CommentEntity?
    fun findByTaskId(taskId: UUID): List<CommentEntity>
    fun findByStoryId(storyId: UUID): List<CommentEntity>
    fun existsByIdAndTaskId(id: UUID, taskId: UUID): Boolean
    fun existsByIdAndStoryId(id: UUID, storyId: UUID): Boolean
}