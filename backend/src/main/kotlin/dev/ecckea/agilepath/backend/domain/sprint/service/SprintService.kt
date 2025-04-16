package dev.ecckea.agilepath.backend.domain.sprint.service

import dev.ecckea.agilepath.backend.domain.sprint.repository.SprintRepository
import dev.ecckea.agilepath.backend.shared.logging.Logged
import org.springframework.stereotype.Service

@Service
class SprintService(
    private val sprintRepository: SprintRepository
) : Logged() {

}