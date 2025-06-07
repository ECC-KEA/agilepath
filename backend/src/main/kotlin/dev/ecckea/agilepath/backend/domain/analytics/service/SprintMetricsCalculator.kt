package dev.ecckea.agilepath.backend.domain.analytics.service

import dev.ecckea.agilepath.backend.domain.analytics.model.*
import dev.ecckea.agilepath.backend.domain.analytics.model.mapper.toSprintInfo
import dev.ecckea.agilepath.backend.domain.project.model.EstimationMethod
import dev.ecckea.agilepath.backend.domain.sprint.model.mapper.toModel
import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEntity
import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskEntity
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import org.springframework.stereotype.Service
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.roundToInt

@Service
class SprintMetricsCalculator(
    private val ctx: RepositoryContext,
    private val taskMetricsCalculator: TaskMetricsCalculator
) {

    fun calculateSprintMetrics(
        sprint: SprintEntity,
        tasks: List<TaskEntity>,
        taskMetrics: List<TaskMetrics>,
        estimationMethod: EstimationMethod
    ): SprintMetrics {
        require(sprint.id != null) { "Sprint ID cannot be null" }

        val completedTasks = taskMetrics.filter { it.isCompleted }

        return SprintMetrics(
            sprintId = sprint.id!!,
            velocity = calculateVelocity(completedTasks, tasks, estimationMethod),
            durationDays = calculateSprintDuration(sprint),
            avgCycleTimeHours = calculateAverageCycleTime(completedTasks),
            longestCycleTimeTaskId = findLongestCycleTimeTask(taskMetrics),
            numReopenedTasks = taskMetrics.count { it.wasReopened },
            numAddedTasksDuringSprint = taskMetrics.count { it.wasAddedDuringSprint },
            numRemovedTasksDuringSprint = taskMetrics.count { it.wasRemovedDuringSprint },
            numReassignedTasks = taskMetrics.count { it.wasReassigned },
            numTasksWithComments = taskMetrics.count { it.commentCount > 0 },
            avgTaskCommentCount = taskMetrics.map { it.commentCount }.average().takeIf { !it.isNaN() } ?: 0.0,
            avgSubtaskCount = taskMetrics.map { it.subtaskCount }.average().takeIf { !it.isNaN() } ?: 0.0,
            totalAssignmentChanges = taskMetrics.sumOf { it.assignmentChanges },
            totalStatusTransitions = taskMetrics.sumOf { it.statusTransitions },
            tasksWithoutComments = taskMetrics.count { it.commentCount == 0 },
            completionRate = if (tasks.isNotEmpty()) (completedTasks.size.toDouble() / tasks.size * 100).roundToInt() else 0,
            tasksByComplexity = categorizeTasksByComplexity(taskMetrics),
            collaborationScore = calculateCollaborationScore(taskMetrics)
        )
    }

    fun calculateSprintMetadata(
        sprint: SprintEntity,
        tasks: List<TaskEntity>,
        estimationMethod: EstimationMethod
    ): SprintMetadata {
        require(sprint.id != null) { "Sprint ID cannot be null" }

        val completedTaskCount = ctx.task.countCompletedTasksForSprint(sprint.id!!)
        val totalPoints = calculateTotalPoints(tasks, estimationMethod)

        return SprintMetadata(
            sprintId = sprint.id!!,
            teamCapacity = sprint.teamCapacity,
            totalTasks = tasks.size,
            completedTasks = completedTaskCount,
            plannedPoints = totalPoints
        )
    }

    // Sprint analysis combining all metrics
    fun generateSprintAnalysis(sprintId: UUID, estimationMethod: EstimationMethod): SprintAnalysis {
        val sprint = ctx.sprint.findOneById(sprintId)
            ?: throw IllegalArgumentException("Sprint not found")
        val tasks = ctx.task.findAllTasksForSprint(sprintId)
        val taskMetrics = taskMetricsCalculator.calculateTaskMetrics(sprint, tasks)
        val sprintMetrics = calculateSprintMetrics(sprint, tasks, taskMetrics, estimationMethod)
        val sprintMetadata = calculateSprintMetadata(sprint, tasks, estimationMethod)

        return SprintAnalysis(
            sprintInfo = sprint.toModel().toSprintInfo(),
            sprintMetrics = sprintMetrics,
            sprintMetadata = sprintMetadata,
            taskMetrics = taskMetrics,
            insights = generateInsights(sprintMetrics, taskMetrics)
        )
    }

    private fun calculateVelocity(
        completedTasks: List<TaskMetrics>,
        allTasks: List<TaskEntity>,
        estimationMethod: EstimationMethod
    ): Int {
        val completedTaskIds = completedTasks.map { it.taskId }.toSet()
        val completedTaskEntities = allTasks.filter { it.id in completedTaskIds }

        return when (estimationMethod) {
            EstimationMethod.STORY_POINTS -> completedTaskEntities
                .mapNotNull { it.estimatePoints?.points }
                .sum()

            EstimationMethod.TSHIRT_SIZES -> completedTaskEntities
                .mapNotNull { it.estimateTshirt?.points }
                .sum()
        }
    }

    private fun calculateTotalPoints(tasks: List<TaskEntity>, estimationMethod: EstimationMethod): Int {
        return when (estimationMethod) {
            EstimationMethod.STORY_POINTS -> tasks
                .mapNotNull { it.estimatePoints?.points }
                .sum()

            EstimationMethod.TSHIRT_SIZES -> tasks
                .mapNotNull { it.estimateTshirt?.points }
                .sum()
        }
    }

    private fun calculateSprintDuration(sprint: SprintEntity): Int {
        return ChronoUnit.DAYS.between(sprint.startDate, sprint.endDate)
            .toInt() + 1 // +1 to include both start and end days
    }

    private fun calculateAverageCycleTime(completedTasks: List<TaskMetrics>): Double {
        val validCycleTimes = completedTasks.map { it.cycleTimeHours }.filter { it > 0 }
        return if (validCycleTimes.isNotEmpty()) {
            validCycleTimes.average()
        } else 0.0
    }

    private fun findLongestCycleTimeTask(taskMetrics: List<TaskMetrics>): UUID? {
        return taskMetrics.maxByOrNull { it.cycleTimeHours }?.taskId
    }

    private fun categorizeTasksByComplexity(taskMetrics: List<TaskMetrics>): TaskComplexityBreakdown {
        val complex = taskMetrics.count { it.subtaskCount > 4 || it.commentCount > 6 }
        val simple = taskMetrics.count { it.subtaskCount <= 1 && it.commentCount <= 2 }
        val moderate = taskMetrics.size - complex - simple  // Everything else

        return TaskComplexityBreakdown(
            simple = simple,
            moderate = moderate,
            complex = complex
        )
    }

    private fun calculateCollaborationScore(taskMetrics: List<TaskMetrics>): Double {
        if (taskMetrics.isEmpty()) return 0.0

        val tasksWithComments = taskMetrics.count { it.commentCount > 0 }
        val avgComments = taskMetrics.map { it.commentCount }.average()
        val tasksWithReassignments = taskMetrics.count { it.assignmentChanges > 1 }

        // Collaboration score: 0-100 based on comment activity and reasonable assignment changes
        val commentScore = (tasksWithComments.toDouble() / taskMetrics.size) * 50
        val engagementScore = minOf(avgComments * 5, 30.0) // Cap at 30 points
        val reassignmentPenalty = (tasksWithReassignments.toDouble() / taskMetrics.size) * 20

        return maxOf(0.0, commentScore + engagementScore - reassignmentPenalty)
    }

    private fun generateInsights(sprintMetrics: SprintMetrics, taskMetrics: List<TaskMetrics>): List<SprintInsight> {
        val insights = mutableListOf<SprintInsight>()

        // Performance insights
        if (sprintMetrics.completionRate < 70) {
            insights.add(SprintInsight.LowCompletionRate(sprintMetrics.completionRate))
        }

        if (sprintMetrics.numReopenedTasks > taskMetrics.size * 0.2) {
            insights.add(SprintInsight.HighReopenRate(sprintMetrics.numReopenedTasks))
        }

        // Collaboration insights
        if (sprintMetrics.collaborationScore < 30) {
            insights.add(SprintInsight.LowCollaboration(sprintMetrics.collaborationScore))
        }

        if (sprintMetrics.numReassignedTasks > taskMetrics.size * 0.3) {
            insights.add(SprintInsight.FrequentReassignments(sprintMetrics.numReassignedTasks))
        }

        // Scope change insights
        if (sprintMetrics.numAddedTasksDuringSprint > 0) {
            insights.add(SprintInsight.ScopeIncrease(sprintMetrics.numAddedTasksDuringSprint))
        }

        return insights
    }
}