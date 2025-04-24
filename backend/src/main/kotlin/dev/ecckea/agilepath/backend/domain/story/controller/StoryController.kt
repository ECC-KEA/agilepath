package dev.ecckea.agilepath.backend.domain.story.controller


import dev.ecckea.agilepath.backend.domain.story.application.StoryApplication
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stories")
@Tag(name = "Stories", description = "Endpoints related to Story management")
class StoryController {
    private val storyApplication: StoryApplication
}