package dev.ecckea.agilepath.backend.shared.context.repository

import dev.ecckea.agilepath.backend.domain.column.repository.SprintColumnRepository
import dev.ecckea.agilepath.backend.domain.column.repository.entity.SprintColumnEntity
import dev.ecckea.agilepath.backend.domain.project.repository.ProjectRepository
import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEntity
import dev.ecckea.agilepath.backend.domain.sprint.repository.SprintRepository
import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEntity
import dev.ecckea.agilepath.backend.domain.story.repository.CommentRepository
import dev.ecckea.agilepath.backend.domain.story.repository.StoryRepository
import dev.ecckea.agilepath.backend.domain.story.repository.SubtaskRepository
import dev.ecckea.agilepath.backend.domain.story.repository.TaskRepository
import dev.ecckea.agilepath.backend.domain.story.repository.entity.CommentEntity
import dev.ecckea.agilepath.backend.domain.story.repository.entity.StoryEntity
import dev.ecckea.agilepath.backend.domain.story.repository.entity.SubtaskEntity
import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskEntity
import dev.ecckea.agilepath.backend.domain.user.repository.UserRepository
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import jakarta.persistence.EntityManager
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

// Generic extension for any JPA repository
fun <T, ID : Any> JpaRepository<T, ID>.ref(id: ID): T = getReferenceById(id)

// Type-safe extensions for each repository
fun StoryRepository.ref(id: UUID): StoryEntity = getReferenceById(id)
fun SprintColumnRepository.ref(id: UUID): SprintColumnEntity = getReferenceById(id)
fun UserRepository.ref(id: String): UserEntity = getReferenceById(id)
fun ProjectRepository.ref(id: UUID): ProjectEntity = getReferenceById(id)
fun SprintRepository.ref(id: UUID): SprintEntity = getReferenceById(id)
fun TaskRepository.ref(id: UUID): TaskEntity = getReferenceById(id)
fun SubtaskRepository.ref(id: UUID): SubtaskEntity = getReferenceById(id)
fun CommentRepository.ref(id: UUID): CommentEntity = getReferenceById(id)


// Special case for composite key
fun EntityManager.refTask(taskId: UUID): TaskEntity = getReference(TaskEntity::class.java, taskId)
fun EntityManager.refUser(userId: String): UserEntity = getReference(UserEntity::class.java, userId)