package dev.ecckea.agilepath.backend.domain.analytics.model.mapper

import dev.ecckea.agilepath.backend.domain.analytics.dto.*
import dev.ecckea.agilepath.backend.domain.analytics.model.*
import dev.ecckea.agilepath.backend.domain.sprint.model.Sprint

fun SprintAnalysis.toDTO(): SprintAnalysisResponse =
    SprintAnalysisResponse(
        sprintInfo = sprintInfo,
        metrics = sprintMetrics.toDTO(),
        metadata = sprintMetadata.toDTO(),
        taskMetrics = taskMetrics.map { it.toDTO() },
        insights = insights.map { it.toDTO() }
    )

fun SprintMetrics.toDTO(): SprintMetricsResponse =
    SprintMetricsResponse(
        sprintId = sprintId,
        velocity = velocity,
        durationDays = durationDays,
        completionRate = completionRate,
        avgCycleTimeHours = avgCycleTimeHours,
        longestCycleTimeTaskId = longestCycleTimeTaskId,
        numReopenedTasks = numReopenedTasks,
        numAddedTasksDuringSprint = numAddedTasksDuringSprint,
        numRemovedTasksDuringSprint = numRemovedTasksDuringSprint,
        numReassignedTasks = numReassignedTasks,
        numTasksWithComments = numTasksWithComments,
        avgTaskCommentCount = avgTaskCommentCount,
        tasksWithoutComments = tasksWithoutComments,
        collaborationScore = collaborationScore,
        avgSubtaskCount = avgSubtaskCount,
        tasksByComplexity = tasksByComplexity.toDTO(),
        totalAssignmentChanges = totalAssignmentChanges,
        totalStatusTransitions = totalStatusTransitions
    )

fun SprintMetadata.toDTO(): SprintMetadataResponse =
    SprintMetadataResponse(
        sprintId = sprintId,
        teamCapacity = teamCapacity,
        totalTasks = totalTasks,
        completedTasks = completedTasks,
        plannedPoints = plannedPoints
    )

fun Sprint.toSprintInfo(): SprintInfo =
    SprintInfo(
        id = id,
        name = name,
        startDate = startDate,
        endDate = endDate,
    )

fun SprintInsight.toDTO(): SprintInsightResponse {
    return when (this) {
        is SprintInsight.LowCompletionRate -> SprintInsightResponse.LowCompletionRate(completionRate)
        is SprintInsight.HighReopenRate -> SprintInsightResponse.HighReopenRate(reopenedTasks)
        is SprintInsight.LowCollaboration -> SprintInsightResponse.LowCollaboration(score)
        is SprintInsight.FrequentReassignments -> SprintInsightResponse.FrequentReassignments(reassignedTasks)
        is SprintInsight.ScopeIncrease -> SprintInsightResponse.ScopeIncrease(addedTasks)
    }
}

fun SprintComparison.toDTO(): SprintComparisonResponse {
    return SprintComparisonResponse(
        current = current.toDTO(),
        previous = previous.toDTO(),
        velocityChange = velocityChange,
        completionRateChange = completionRateChange,
        avgCycleTimeChange = avgCycleTimeChange,
        collaborationScoreChange = collaborationScoreChange
    )
}

fun SprintTrends.toDTO(): SprintTrendsResponse {
    return SprintTrendsResponse(
        sprints = sprints.map { it.toDTO() },
        avgVelocity = avgVelocity,
        avgCompletionRate = avgCompletionRate,
        avgCycleTime = avgCycleTime,
        avgCollaborationScore = avgCollaborationScore,
        velocityTrend = velocityTrend.toDTO(),
        completionRateTrend = completionRateTrend.toDTO()
    )
}

fun TaskMetrics.toDTO(): TaskMetricsResponse =
    TaskMetricsResponse(
        taskId = taskId,
        cycleTimeHours = cycleTimeHours,
        wasReopened = wasReopened,
        wasAddedDuringSprint = wasAddedDuringSprint,
        wasRemovedDuringSprint = wasRemovedDuringSprint,
        wasReassigned = wasReassigned,
        commentCount = commentCount,
        isCompleted = isCompleted,
        subtaskCount = subtaskCount,
        timeToFirstComment = timeToFirstComment,
        lastActivityTime = lastActivityTime,
        assignmentChanges = assignmentChanges,
        statusTransitions = statusTransitions
    )

fun TaskComplexityBreakdown.toDTO(): TaskComplexityResponse =
    TaskComplexityResponse(
        simple = simple,
        moderate = moderate,
        complex = complex
    )

fun TrendDirection.toDTO(): String {
    return when (this) {
        TrendDirection.IMPROVING -> "IMPROVING"
        TrendDirection.STABLE -> "STABLE"
        TrendDirection.DECLINING -> "DECLINING"
    }
}

fun BurndownData.toDTO(): BurndownResponse =
    BurndownResponse(
        sprintId = sprintId,
        totalWork = totalWork,
        optimalPath = optimalPath,
        actualProgress = actualProgress,
        startDate = startDate,
        endDate = endDate
    )
