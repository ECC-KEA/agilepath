package dev.ecckea.agilepath.backend.domain.assistant.service

import dev.ecckea.agilepath.backend.domain.assistant.model.Assistant
import dev.ecckea.agilepath.backend.domain.assistant.model.NewAssistant
import dev.ecckea.agilepath.backend.domain.assistant.model.mapper.toEntity
import dev.ecckea.agilepath.backend.domain.assistant.model.mapper.toModel
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AssistantService(
    private val ctx: RepositoryContext
) : Logged() {
    fun getAssistants(): List<Assistant> {
        return ctx.assistant.findAll().map { it.toModel() }
    }

    fun getAssistant(assistantId: UUID): Assistant {
        return ctx.assistant.findById(assistantId)
            .orElseThrow { IllegalArgumentException("Assistant with ID $assistantId not found") }
            .toModel()
    }

    fun createAssistant(newAssistant: NewAssistant): Assistant {
        log.info("Creating assistant")
        if(ctx.assistant.existsByName(newAssistant.name)) {
            throw IllegalArgumentException("Assistant with name ${newAssistant.name} already exists")
        }
        val entity = newAssistant.toEntity()
        val saved = ctx.assistant.save(entity)
        return saved.toModel()
    }
}