/**
 * Provides extension functions that simplify retrieving entity references in a JPA context.
 *
 * These extension functions help reduce boilerplate code when working with entity references.
 * They return proxy objects that don't load data from the database until accessed (lazy loading),
 * which is particularly useful for establishing relationships between entities efficiently.
 *
 * All repository extension functions follow the same pattern: they wrap the standard
 * JpaRepository.getReferenceById() method with a shorter, more readable syntax.
 *
 * Example usage:
 * ```
 * // Instead of
 * val projectRef = projectRepository.getReferenceById(projectId)
 *
 * // You can use
 * val projectRef = projectRepository.ref(projectId)
 * ```
 *
 * Note: All these functions will throw EntityNotFoundException if no entity with the given ID exists.
 */
package dev.ecckea.agilepath.backend.shared.context.repository

import dev.ecckea.agilepath.backend.domain.column.repository.SprintColumnRepository
import dev.ecckea.agilepath.backend.domain.column.repository.entity.SprintColumnEntity
import dev.ecckea.agilepath.backend.domain.project.repository.ProjectRepository
import dev.ecckea.agilepath.backend.domain.project.repository.UserProjectRepository
import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEntity
import dev.ecckea.agilepath.backend.domain.project.repository.entity.UserProjectEntity
import dev.ecckea.agilepath.backend.domain.project.repository.entity.UserProjectId
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

/**
 * Generic extension function for any JPA repository to retrieve a reference to an entity by its ID.
 *
 * This function provides a more concise way to call [JpaRepository.getReferenceById]. It returns a
 * proxy to the entity without loading the entity data from the database until the proxy is accessed.
 * This is useful for creating relationships between entities without loading full entity data.
 *
 * @param T The type of the entity.
 * @param ID The type of the entity's ID.
 * @param id The ID of the entity to retrieve.
 * @return A reference proxy to the entity with the given ID.
 * @throws EntityNotFoundException If no entity with the given ID exists.
 *
 * @sample
 * ```
 * // Instead of
 * val projectRef = projectRepository.getReferenceById(projectId)
 *
 * // You can use
 * val projectRef = projectRepository.ref(projectId)
 * ```
 */
fun <T, ID : Any> JpaRepository<T, ID>.ref(id: ID): T = getReferenceById(id)

/** Get a reference to a [StoryEntity] by UUID. */
fun StoryRepository.ref(id: UUID): StoryEntity = getReferenceById(id)

/** Get a reference to a [SprintColumnEntity] by UUID. */
fun SprintColumnRepository.ref(id: UUID): SprintColumnEntity = getReferenceById(id)

/** Get a reference to a [UserEntity] by String ID. */
fun UserRepository.ref(id: String): UserEntity = getReferenceById(id)

/** Get a reference to a [ProjectEntity] by UUID. */
fun ProjectRepository.ref(id: UUID): ProjectEntity = getReferenceById(id)

/** Get a reference to a [UserProject] by String and UUID. */
fun UserProjectRepository.ref(userId: String, projectId: UUID): UserProjectEntity =
    getReferenceById(UserProjectId(userId, projectId))

/** Get a reference to a [SprintEntity] by UUID. */
fun SprintRepository.ref(id: UUID): SprintEntity = getReferenceById(id)

/** Get a reference to a [TaskEntity] by UUID. */
fun TaskRepository.ref(id: UUID): TaskEntity = getReferenceById(id)

/** Get a reference to a [SubtaskEntity] by UUID. */
fun SubtaskRepository.ref(id: UUID): SubtaskEntity = getReferenceById(id)

/** Get a reference to a [CommentEntity] by UUID. */
fun CommentRepository.ref(id: UUID): CommentEntity = getReferenceById(id)

// EntityManager extension functions
// These functions use EntityManager.getReference() instead of getReferenceById()
// but provide the same lazy-loading proxy behavior.

/**
 * Get a reference to a [TaskEntity] by UUID using EntityManager.
 * Returns a proxy that loads data only when accessed.
 */
fun EntityManager.refTask(taskId: UUID): TaskEntity = getReference(TaskEntity::class.java, taskId)

/**
 * Get a reference to a [UserEntity] by String ID using EntityManager.
 * Returns a proxy that loads data only when accessed.
 */
fun EntityManager.refUser(userId: String): UserEntity = getReference(UserEntity::class.java, userId)