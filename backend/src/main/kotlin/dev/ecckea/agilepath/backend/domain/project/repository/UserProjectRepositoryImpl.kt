package dev.ecckea.agilepath.backend.domain.project.repository


import dev.ecckea.agilepath.backend.domain.project.model.ProjectRole
import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEntity
import dev.ecckea.agilepath.backend.domain.project.repository.entity.UserProjectEntity
import dev.ecckea.agilepath.backend.domain.project.repository.entity.UserProjectId
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class UserProjectRepositoryImpl(
    @PersistenceContext private val entityManager: EntityManager
) : UserProjectRepositoryCustom {

    @Transactional
    override fun addUserToProject(userId: String, projectId: UUID, role: ProjectRole) {
        val userRef = entityManager.getReference(UserEntity::class.java, userId)
        val projectRef = entityManager.getReference(ProjectEntity::class.java, projectId)
        val entity = UserProjectEntity(user = userRef, project = projectRef, role = role)
        entityManager.persist(entity)
    }

    @Transactional
    override fun updateUserProjectRole(userId: String, projectId: UUID, role: ProjectRole) {
        val id = UserProjectId(user = userId, project = projectId)
        val existing = entityManager.find(UserProjectEntity::class.java, id)
        existing?.let {
            entityManager.remove(existing)
            val updated = existing.copy(role = role)
            entityManager.persist(updated)
        }
    }

    @Transactional
    override fun removeUserFromProject(userId: String, projectId: UUID) {
        val id = UserProjectId(user = userId, project = projectId)
        val entity = entityManager.find(UserProjectEntity::class.java, id)
        entity?.let { entityManager.remove(it) }
    }

    @Transactional(readOnly = true)
    override fun getProjectsForUser(userId: String): List<ProjectEntity> {
        return entityManager.createQuery(
            """
        SELECT up.project
        FROM UserProjectEntity up
        WHERE up.user.id = :userId
        """.trimIndent(), ProjectEntity::class.java
        ).setParameter("userId", userId)
            .resultList
    }

    @Transactional(readOnly = true)
    override fun findAllByProjectId(projectId: UUID): List<UserProjectEntity> {
        return entityManager.createQuery(
            """
        SELECT up
        FROM UserProjectEntity up
        WHERE up.project.id = :projectId
        """.trimIndent(), UserProjectEntity::class.java
        ).setParameter("projectId", projectId)
            .resultList
    }
}
