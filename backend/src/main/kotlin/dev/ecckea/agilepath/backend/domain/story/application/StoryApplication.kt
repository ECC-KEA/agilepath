package dev.ecckea.agilepath.backend.domain.story.application

import dev.ecckea.agilepath.backend.domain.story.model.NewStory
import dev.ecckea.agilepath.backend.domain.story.model.Story
import dev.ecckea.agilepath.backend.domain.story.model.StoryEventType
import dev.ecckea.agilepath.backend.domain.story.service.CommentService
import dev.ecckea.agilepath.backend.domain.story.service.StoryService
import dev.ecckea.agilepath.backend.domain.story.service.TaskService
import dev.ecckea.agilepath.backend.domain.user.service.UserService
import dev.ecckea.agilepath.backend.shared.logging.events.StoryEventLogger
import dev.ecckea.agilepath.backend.shared.security.currentUser
import dev.ecckea.agilepath.backend.shared.utils.ChangeDetector
import org.springframework.stereotype.Service
import java.util.*

@Service
class StoryApplication(
    private val storyService: StoryService,
    private val taskService: TaskService,
    private val commentService: CommentService,
    private val userService: UserService,
    private val eventLogger: StoryEventLogger
) {

    fun createStory(newStory: NewStory): Story {
        val createdStory = storyService.createStory(newStory)
        val user = userService.get(currentUser())

        if (createdStory.acceptanceCriteria != null) {
            eventLogger.logEvent(
                entityId = createdStory.id,
                eventType = StoryEventType.ACCEPTANCE_CRITERIA_ADDED,
                triggeredBy = user,
                oldValue = null,
                newValue = createdStory.acceptanceCriteria
            )
        }

        eventLogger.logEvent(
            entityId = createdStory.id,
            eventType = StoryEventType.CREATED,
            triggeredBy = user,
            oldValue = null,
            newValue = createdStory.title
        )

        return createdStory
    }

    fun getStory(id: UUID): Story {
        val story = storyService.getStory(id)
        val tasks = taskService.getTasksByStoryId(id)
        val comments = commentService.getCommentsByStoryId(id)
        return story.copy(tasks = tasks, comments = comments)
    }

    fun updateStory(id: UUID, newStory: NewStory): Story {
        val oldStory = storyService.getStory(id)
        val updatedStory = storyService.updateStory(id, newStory, currentUser().id)

        logStoryChangeEvents(id, oldStory, updatedStory)

        return updatedStory
    }

    fun deleteStory(id: UUID) {
        val storyToDelete = storyService.getStory(id)
        val user = userService.get(currentUser())

        eventLogger.logEvent(
            entityId = id,
            eventType = StoryEventType.DELETED,
            triggeredBy = user,
            oldValue = storyToDelete.title,
            newValue = null
        )

        storyService.deleteStory(id)
    }

    fun getStoriesByProjectId(projectId: UUID): List<Story> {
        return storyService.getStoriesByProjectId(projectId)
    }

    private fun logStoryChangeEvents(id: UUID, oldStory: Story, updatedStory: Story) {
        val changeResult = ChangeDetector.detectChanges(
            old = oldStory,
            new = updatedStory,
            excludedProperties = setOf("id", "createdAt", "updatedAt", "projectId", "tasks", "comments")
        )

        if (!changeResult.hasChanges()) return

        val user = userService.get(currentUser())

        changeResult.changes.forEach { propertyChange ->
            when (propertyChange.propertyName) {
                "title" -> eventLogger.logEvent(
                    entityId = id,
                    eventType = StoryEventType.TITLE_CHANGED,
                    triggeredBy = user,
                    oldValue = propertyChange.oldValue?.toString(),
                    newValue = propertyChange.newValue?.toString()
                )

                "description" -> eventLogger.logEvent(
                    entityId = id,
                    eventType = StoryEventType.DESCRIPTION_CHANGED,
                    triggeredBy = user,
                    oldValue = propertyChange.oldValue?.toString(),
                    newValue = propertyChange.newValue?.toString()
                )

                "acceptanceCriteria" -> eventLogger.logEvent(
                    entityId = id,
                    eventType = StoryEventType.ACCEPTANCE_CRITERIA_CHANGED,
                    triggeredBy = user,
                    oldValue = propertyChange.oldValue?.toString(),
                    newValue = propertyChange.newValue?.toString()
                )

                "status" -> eventLogger.logEvent(
                    entityId = id,
                    eventType = StoryEventType.STATUS_CHANGED,
                    triggeredBy = user,
                    oldValue = propertyChange.oldValue?.toString(),
                    newValue = propertyChange.newValue?.toString()
                )

                "priority" -> eventLogger.logEvent(
                    entityId = id,
                    eventType = StoryEventType.PRIORITY_CHANGED,
                    triggeredBy = user,
                    oldValue = propertyChange.oldValue?.toString(),
                    newValue = propertyChange.newValue?.toString()
                )
            }
        }

        if (changeResult.contains("acceptanceCriteria") && updatedStory.acceptanceCriteria.isNullOrEmpty()) {
            eventLogger.logEvent(
                entityId = id,
                eventType = StoryEventType.ACCEPTANCE_CRITERIA_REMOVED,
                triggeredBy = user,
                oldValue = oldStory.acceptanceCriteria,
                newValue = updatedStory.acceptanceCriteria
            )
        }

        if (changeResult.contains("status") && updatedStory.status == "COMPLETED") {
            eventLogger.logEvent(
                entityId = id,
                eventType = StoryEventType.COMPLETED,
                triggeredBy = user,
                oldValue = oldStory.status,
                newValue = updatedStory.status
            )
        }

        if (changeResult.contains("status") && oldStory.status == "COMPLETED") {
            eventLogger.logEvent(
                entityId = id,
                eventType = StoryEventType.REOPENED,
                triggeredBy = user,
                oldValue = oldStory.status,
                newValue = updatedStory.status
            )
        }
    }
}