package dev.ecckea.agilepath.backend.domain.assistant.application

import dev.ecckea.agilepath.backend.domain.assistant.model.Assistant
import dev.ecckea.agilepath.backend.domain.assistant.model.NewAssistant
import dev.ecckea.agilepath.backend.domain.assistant.service.AssistantService
import org.springframework.stereotype.Service
import java.util.*

@Service
class AssistantApplication(
    private val assistantService: AssistantService,
) {
    fun getAssistants(): List<Assistant> {
        return assistantService.getAssistants()
    }

    fun getAssistant(assistantName: String): Assistant {
        return assistantService.getAssistant(assistantName)
    }

    fun createAssistant(newAssistant: NewAssistant): Assistant {
        return assistantService.createAssistant(newAssistant)
    }
}