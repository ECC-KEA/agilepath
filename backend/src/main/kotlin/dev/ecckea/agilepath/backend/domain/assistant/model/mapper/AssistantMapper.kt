package dev.ecckea.agilepath.backend.domain.assistant.model.mapper
import dev.ecckea.agilepath.backend.domain.assistant.dto.AssistantResponse
import dev.ecckea.agilepath.backend.domain.assistant.model.Assistant
import dev.ecckea.agilepath.backend.domain.assistant.model.NewAssistant
import dev.ecckea.agilepath.backend.domain.assistant.dto.AssistantRequest
import dev.ecckea.agilepath.backend.domain.assistant.repository.entity.AssistantEntity
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException

// Entity -> Domain Model
fun AssistantEntity.toModel(): Assistant {
    val entityId = id ?: throw ResourceNotFoundException("SprintColumn ID cannot be null.")
    val description = description ?: throw ResourceNotFoundException("SprintColumn ID cannot be null.")
    val prompt = prompt ?: throw ResourceNotFoundException("SprintColumn ID cannot be null.")

    return Assistant(
        id = entityId,
        name = name,
        description = description,
        model = model,
        prompt = prompt,
        temperature = temperature,
        topP = topP,
        maxTokens = maxTokens
    )
}

// Domain Model -> Response DTO
fun Assistant.toDTO(): AssistantResponse = AssistantResponse(
    id = id.toString(),
    name = name,
    description = description,
    model = model,
    prompt = prompt,
    temperature = temperature,
    topP = topP,
    maxTokens = maxTokens
)

// Request DTO -> NewAssistant (Domain Model)
fun AssistantRequest.toModel(): NewAssistant = NewAssistant(
    name = name,
    description = description,
    model = model,
    prompt = prompt,
    temperature = temperature,
    topP = topP,
    maxTokens = maxTokens
)

// NewAssistant -> Entity
fun NewAssistant.toEntity(): AssistantEntity = AssistantEntity(
    id = null,  // New entity, so ID is null
    name = name,
    description = description,
    model = model,
    prompt = prompt,
    temperature = temperature?: 0.7,
    topP = topP?: 1.0,
    maxTokens = maxTokens?: 1000,
)

// Domain Model -> Entity
fun Assistant.toEntity(): AssistantEntity = AssistantEntity(
    id = id,
    name = name,
    description = description,
    model = model,
    prompt = prompt,
    temperature = temperature,
    topP = topP,
    maxTokens = maxTokens
)

// Update existing entity with NewAssistant data
fun AssistantEntity.updatedWith(update: NewAssistant): AssistantEntity {
    return AssistantEntity(
        id = this.id,
        name = update.name,
        description = update.description,
        model = update.model,
        prompt = update.prompt,
        temperature = update.temperature?: 0.7,
        topP = update.topP?: 1.0,
        maxTokens = update.maxTokens?: 1000
    )
}






