package dev.ecckea.agilepath.backend.domain.retrospective.model.mapper

import dev.ecckea.agilepath.backend.domain.retrospective.dto.RetrospectiveRequest
import dev.ecckea.agilepath.backend.domain.retrospective.dto.RetrospectiveResponse
import dev.ecckea.agilepath.backend.domain.retrospective.model.NewRetrospective
import dev.ecckea.agilepath.backend.domain.retrospective.model.Retrospective
import dev.ecckea.agilepath.backend.domain.retrospective.model.TalkingPoint
import dev.ecckea.agilepath.backend.domain.retrospective.repository.entity.RetrospectiveEntity
import dev.ecckea.agilepath.backend.domain.retrospective.repository.entity.TalkingPointEmbeddable
import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEntity


fun RetrospectiveEntity.toModel(): Retrospective {
    val sprintId = sprint.id ?: throw IllegalArgumentException("RetrospectiveEntity must have a valid ID")
    val id = this.id ?: throw IllegalArgumentException("RetrospectiveEntity must have a valid ID")
    return Retrospective(
        id = id,
        sprintId = sprintId,
        talkingPoints = this.talkingPoints.map { it.toModel() },
        completedAt = this.completedAt,
        teamMood = this.teamMood,
        keepDoing = this.keepDoing,
        stopDoing = this.stopDoing,
        startDoing = this.startDoing
    )
}

fun TalkingPointEmbeddable.toModel(): TalkingPoint {
    return TalkingPoint(
        prompt =  this.prompt,
        response = this.response,
    )
}

fun Retrospective.toEntity(sprintEntity: SprintEntity): RetrospectiveEntity {
    return RetrospectiveEntity(
        id = this.id,
        sprint = sprintEntity,
        talkingPoints = this.talkingPoints.map { it.toEmbeddable() },
        teamMood = this.teamMood,
        keepDoing = this.keepDoing,
        stopDoing = this.stopDoing,
        startDoing = this.startDoing
    )
}

fun Retrospective.toDTO() = RetrospectiveResponse (
    id = id,
    sprintId = sprintId,
    completedAt = completedAt,
    talkingPoints = talkingPoints,
    teamMood = teamMood,
    keepDoing = keepDoing,
    stopDoing = stopDoing,
    startDoing = startDoing
)

fun TalkingPoint.toEmbeddable(): TalkingPointEmbeddable {
    return TalkingPointEmbeddable(
        prompt = this.prompt,
        response = this.response
    )
}

fun RetrospectiveRequest.toModel(): NewRetrospective {
    return NewRetrospective(
        sprintId = this.sprintId,
        talkingPoints = this.talkingPoints,
        teamMood = this.teamMood,
        keepDoing = this.keepDoing,
        stopDoing = this.stopDoing,
        startDoing = this.startDoing
    )
}

fun NewRetrospective.toEntity(sprintEntity: SprintEntity): RetrospectiveEntity {
    return RetrospectiveEntity(
        id = null,
        sprint = sprintEntity,
        talkingPoints = this.talkingPoints.map { it.toEmbeddable() },
        teamMood = this.teamMood,
        keepDoing = this.keepDoing,
        stopDoing = this.stopDoing,
        startDoing = this.startDoing
    )
}