package dev.ecckea.agilepath.backend.domain.story.application

import dev.ecckea.agilepath.backend.domain.story.service.CommentService
import dev.ecckea.agilepath.backend.domain.story.service.StoryService
import dev.ecckea.agilepath.backend.domain.story.service.TaskService
import org.springframework.stereotype.Service

@Service
class StoryApplication(
    private val storyService: StoryService,
    private val taskService: TaskService,
    private val commentService: CommentService,
) {

}