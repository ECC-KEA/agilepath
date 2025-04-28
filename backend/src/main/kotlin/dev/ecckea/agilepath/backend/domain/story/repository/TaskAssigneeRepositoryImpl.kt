package dev.ecckea.agilepath.backend.domain.story.repository

import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskAssigneeEntity
import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskAssigneeId
import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskEntity
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class TaskAssigneeRepositoryImpl(
    @PersistenceContext private val entityManager: EntityManager
) : TaskAssigneeRepositoryCustom {

    @Transactional
    override fun addAssignee(taskId: UUID, userId: String) {
        val taskRef = entityManager.getReference(TaskEntity::class.java, taskId)
        val userRef = entityManager.getReference(UserEntity::class.java, userId)

        val taskAssignee = TaskAssigneeEntity(
            task = taskRef,
            user = userRef
        )
        entityManager.persist(taskAssignee)
    }

    @Transactional
    override fun removeAssignee(taskId: UUID, userId: String) {
        val taskAssigneeId = TaskAssigneeId(task = taskId, user = userId)
        val taskAssignee = entityManager.find(TaskAssigneeEntity::class.java, taskAssigneeId)
        taskAssignee?.let {
            entityManager.remove(it)
        }
    }

    @Transactional(readOnly = true)
    override fun getAssignees(taskId: UUID): List<String> {
        return entityManager.createQuery(
            """
            SELECT ta.user.id 
            FROM TaskAssigneeEntity ta
            WHERE ta.task.id = :taskId
            """.trimIndent(), String::class.java
        )
            .setParameter("taskId", taskId)
            .resultList
    }

    @Transactional(readOnly = true)
    override fun getTasksByAssignee(userId: String): List<UUID> {
        return entityManager.createQuery(
            """
            SELECT ta.task.id
            FROM TaskAssigneeEntity ta
            WHERE ta.user.id = :userId
            """.trimIndent(), UUID::class.java
        )
            .setParameter("userId", userId)
            .resultList
    }
}
