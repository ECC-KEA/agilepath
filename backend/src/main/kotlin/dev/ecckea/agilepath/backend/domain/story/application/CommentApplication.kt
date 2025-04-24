package dev.ecckea.agilepath.backend.domain.story.application

import dev.ecckea.agilepath.backend.domain.story.service.CommentService

class CommentApplication(
    private val commentService: CommentService,
) {
}