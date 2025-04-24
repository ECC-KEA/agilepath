package dev.ecckea.agilepath.backend.domain.story.application

import dev.ecckea.agilepath.backend.domain.story.service.SubtaskService
import org.springframework.stereotype.Service

@Service
class SubtaskApplication(
    private val subtaskService: SubtaskService,
) {

}