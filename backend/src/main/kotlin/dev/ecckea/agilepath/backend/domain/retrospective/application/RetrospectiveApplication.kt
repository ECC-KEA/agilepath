package dev.ecckea.agilepath.backend.domain.retrospective.application

import dev.ecckea.agilepath.backend.domain.retrospective.model.NewRetrospective
import dev.ecckea.agilepath.backend.domain.retrospective.model.Retrospective
import dev.ecckea.agilepath.backend.domain.retrospective.service.RetrospectiveService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class RetrospectiveApplication(
    private val retrospectiveService: RetrospectiveService
) {
    fun getRetrospective(sprintId: UUID): Retrospective {
        return retrospectiveService.getRetrospective(sprintId)
    }

    fun createRetrospective(retrospective: NewRetrospective): Retrospective {
        return retrospectiveService.createRetrospective(retrospective)
    }

}