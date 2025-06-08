package dev.ecckea.agilepath.backend.infrastructure.cache

import dev.ecckea.agilepath.backend.domain.retrospective.model.Retrospective
import com.fasterxml.jackson.core.type.TypeReference
import dev.ecckea.agilepath.backend.domain.sprint.model.Sprint
import java.util.*

fun CacheService.getRetrospective(sprintId: UUID): Retrospective? {
    val key = CacheKeys.retrospectiveKey(sprintId.toString())
    return getFromCache(key, Retrospective::class.java, "retrospective for sprint $sprintId")
}

fun CacheService.cacheRetrospective(retrospective: Retrospective, ttlMinutes: Long = 15) {
    val key = CacheKeys.retrospectiveKey(retrospective.sprintId.toString())
    setInCache(key, retrospective, ttlMinutes, "retrospective for sprint ${retrospective.sprintId}")
}