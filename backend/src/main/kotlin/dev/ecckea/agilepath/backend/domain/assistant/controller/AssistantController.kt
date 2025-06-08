package dev.ecckea.agilepath.backend.domain.assistant.controller

import dev.ecckea.agilepath.backend.domain.assistant.application.AssistantApplication
import dev.ecckea.agilepath.backend.domain.assistant.dto.AssistantResponse
import dev.ecckea.agilepath.backend.domain.assistant.model.NewAssistant
import dev.ecckea.agilepath.backend.domain.assistant.model.mapper.toDTO
import dev.ecckea.agilepath.backend.shared.logging.Logged
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Assistant", description = "Endpoints related to LLM assistant management")
class AssistantController(
    private val assistantApplication: AssistantApplication,
) : Logged() {

    @GetMapping("/assistants")
    fun getAssistants(): List<AssistantResponse> {
        log.info("GET /assistants - Get assistants")
        return assistantApplication.getAssistants().map { it.toDTO() }
    }

    @GetMapping("/assistants/{assistantName}")
    fun getAssistant(@PathVariable assistantName: String): AssistantResponse {
        log.info("GET /assistants/{} - Get assistant", assistantName)
        return assistantApplication.getAssistant(assistantName).toDTO()
    }

    @PostMapping("/assistants")
    fun createAssistant(@RequestBody newAssistant: NewAssistant): AssistantResponse {
        log.info("POST /assistants - Create assistant")
        return assistantApplication.createAssistant(newAssistant).toDTO()
    }
}