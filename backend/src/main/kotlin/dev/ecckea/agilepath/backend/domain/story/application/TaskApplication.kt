package dev.ecckea.agilepath.backend.domain.story.application

import dev.ecckea.agilepath.backend.domain.story.service.TaskService
import org.springframework.stereotype.Service

@Service
class TaskApplication(
    private val taskService: TaskService,
) {
}