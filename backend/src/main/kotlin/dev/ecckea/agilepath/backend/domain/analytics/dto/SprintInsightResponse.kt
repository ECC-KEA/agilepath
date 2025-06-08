package dev.ecckea.agilepath.backend.domain.analytics.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = SprintInsightResponse.LowCompletionRate::class, name = "LowCompletionRate"),
    JsonSubTypes.Type(value = SprintInsightResponse.HighReopenRate::class, name = "HighReopenRate"),
    JsonSubTypes.Type(value = SprintInsightResponse.LowCollaboration::class, name = "LowCollaboration"),
    JsonSubTypes.Type(value = SprintInsightResponse.FrequentReassignments::class, name = "FrequentReassignments"),
    JsonSubTypes.Type(value = SprintInsightResponse.ScopeIncrease::class, name = "ScopeIncrease")
)
sealed class SprintInsightResponse {
    data class LowCompletionRate(val completionRate: Int) : SprintInsightResponse()
    data class HighReopenRate(val reopenedTasks: Int) : SprintInsightResponse()
    data class LowCollaboration(val score: Double) : SprintInsightResponse()
    data class FrequentReassignments(val reassignedTasks: Int) : SprintInsightResponse()
    data class ScopeIncrease(val addedTasks: Int) : SprintInsightResponse()
}
