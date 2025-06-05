package dev.ecckea.agilepath.backend.domain.analytics.model

/**
 * Represents insights derived from sprint analytics.
 *
 * This sealed class is used to categorize different types of insights
 * that can be identified during sprint analysis. Each subclass represents
 * a specific type of insight with relevant data.
 */
sealed class SprintInsight {
    data class LowCompletionRate(val completionRate: Int) : SprintInsight()
    data class HighReopenRate(val reopenedTasks: Int) : SprintInsight()
    data class LowCollaboration(val score: Double) : SprintInsight()
    data class FrequentReassignments(val reassignedTasks: Int) : SprintInsight()
    data class ScopeIncrease(val addedTasks: Int) : SprintInsight()
}