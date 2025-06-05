package dev.ecckea.agilepath.backend.domain.analytics.dto

sealed class SprintInsightResponse {
    data class LowCompletionRate(val completionRate: Int) : SprintInsightResponse()
    data class HighReopenRate(val reopenedTasks: Int) : SprintInsightResponse()
    data class LowCollaboration(val score: Double) : SprintInsightResponse()
    data class FrequentReassignments(val reassignedTasks: Int) : SprintInsightResponse()
    data class ScopeIncrease(val addedTasks: Int) : SprintInsightResponse()
}
