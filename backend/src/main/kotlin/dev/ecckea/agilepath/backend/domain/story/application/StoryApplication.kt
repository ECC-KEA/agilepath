package dev.ecckea.agilepath.backend.domain.story.application

import dev.ecckea.agilepath.backend.domain.story.model.NewStory
import dev.ecckea.agilepath.backend.domain.story.model.Story
import dev.ecckea.agilepath.backend.domain.story.service.CommentService
import dev.ecckea.agilepath.backend.domain.story.service.StoryService
import dev.ecckea.agilepath.backend.domain.story.service.TaskService
import dev.ecckea.agilepath.backend.shared.security.currentUser
import org.springframework.stereotype.Service
import java.util.*

@Service
class StoryApplication(
    private val storyService: StoryService,
    private val taskService: TaskService,
    private val commentService: CommentService
) {

    fun createStory(newStory: NewStory): Story {
        return storyService.createStory(newStory)
    }

    fun getStory(id: UUID): Story {
        val story = storyService.getStory(id)
        val tasks = taskService.getTasksByStoryId(id)
        val comments = commentService.getCommentsByStoryId(id)
        return story.copy(tasks = tasks, comments = comments)
    }

    fun updateStory(id: UUID, newStory: NewStory): Story {
        return storyService.updateStory(id, newStory, currentUser().id)
    }

    fun deleteStory(id: UUID) {
        storyService.deleteStory(id)
    }
}