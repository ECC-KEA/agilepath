package dev.ecckea.agilepath.backend.domain.analytics.service

import dev.ecckea.agilepath.backend.domain.analytics.model.*
import dev.ecckea.agilepath.backend.domain.project.model.EstimationMethod
import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEntity
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import dev.ecckea.agilepath.backend.shared.exceptions.ComparisonException
import dev.ecckea.agilepath.backend.shared.exceptions.ResourceNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class AnalyticsService(
    private val ctx: RepositoryContext,
    private val taskMetricsCalculator: TaskMetricsCalculator,
    private val sprintMetricsCalculator: SprintMetricsCalculator,
    private val burndownCalculator: BurndownCalculator
) {

    // SPRINT-LEVEL ANALYTICS

    /**
     * Get comprehensive sprint analysis with all metrics, metadata, and insights
     */
    fun getSprintAnalysis(sprintId: UUID): SprintAnalysis {
        val estimationMethod = getEstimationMethod(sprintId)

        return sprintMetricsCalculator.generateSprintAnalysis(sprintId, estimationMethod)
    }

    /**
     * Get sprint metrics only (performance data)
     */
    fun getSprintMetrics(sprintId: UUID): SprintMetrics {
        val sprint = getSprint(sprintId)
        val estimationMethod = getEstimationMethod(sprintId)
        val tasks = ctx.task.findAllTasksForSprint(sprintId)
        val taskMetrics = taskMetricsCalculator.calculateTaskMetrics(sprint, tasks)

        return sprintMetricsCalculator.calculateSprintMetrics(
            sprint, tasks, taskMetrics, estimationMethod
        )
    }

    /**
     * Get sprint metadata only (basic counts and capacity)
     */
    fun getSprintMetadata(sprintId: UUID): SprintMetadata {
        val sprint = getSprint(sprintId)
        val estimationMethod = getEstimationMethod(sprintId)
        val tasks = ctx.task.findAllTasksForSprint(sprintId)

        return sprintMetricsCalculator.calculateSprintMetadata(
            sprint, tasks, estimationMethod
        )
    }

    // TASK-LEVEL ANALYTICS

    /**
     * Get metrics for all tasks in a sprint
     */
    fun getTaskMetrics(sprintId: UUID): List<TaskMetrics> {
        val sprint = getSprint(sprintId)
        val tasks = ctx.task.findAllTasksForSprint(sprintId)
        return taskMetricsCalculator.calculateTaskMetrics(sprint, tasks)
    }

    /**
     * Get metrics for a specific task
     */
    fun getTaskMetrics(sprintId: UUID, taskId: UUID): TaskMetrics {
        val sprint = getSprint(sprintId)

        val task = ctx.task.findOneById(taskId)
            ?: throw IllegalArgumentException("Task not found: $taskId")

        return taskMetricsCalculator.calculateTaskMetrics(sprint, listOf(task)).first()
    }

    // BURNDOWN CHART ANALYTICS

    /**
     * Get burndown chart data with actual progress and optimal path
     */
    fun getBurndownData(sprintId: UUID): BurndownData {
        val estimationMethod = getEstimationMethod(sprintId)
        val sprint = getSprint(sprintId)

        val tasks = ctx.task.findAllTasksForSprint(sprintId)
        val taskEvents = ctx.taskEvent.findAllByTaskIdIn(tasks.mapNotNull { it.id })

        return burndownCalculator.calculateBurndown(
            sprint, tasks, taskEvents, estimationMethod
        )
    }

    // COMPARATIVE ANALYTICS

    /**
     * Compare current sprint to previous sprint
     */
    fun compareSprintToPrevious(currentSprintId: UUID): SprintComparison {
        val currentSprint = getSprint(currentSprintId)

        val currentSprintProjectId = currentSprint.project.id
            ?: throw IllegalArgumentException("Sprint does not belong to a project: $currentSprintId")

        val previousSprint = ctx.sprint.findPreviousSprintInProject(currentSprintProjectId, currentSprint.startDate)
            ?: throw ComparisonException("No previous sprint found for comparison")

        val currentMetrics = getSprintMetrics(currentSprintId)
        val previousMetrics = getSprintMetrics(previousSprint.id!!)

        return SprintComparison(
            current = currentMetrics,
            previous = previousMetrics,
            velocityChange = currentMetrics.velocity - previousMetrics.velocity,
            completionRateChange = currentMetrics.completionRate - previousMetrics.completionRate,
            avgCycleTimeChange = currentMetrics.avgCycleTimeHours - previousMetrics.avgCycleTimeHours,
            collaborationScoreChange = currentMetrics.collaborationScore - previousMetrics.collaborationScore
        )
    }

    /**
     * Get sprint trends over the last N sprints
     */
    fun getSprintTrends(projectId: UUID, sprintCount: Int = 3): SprintTrends {
        val recentSprints = ctx.sprint.findRecentSprintsInProject(projectId, sprintCount)
        val sprintMetrics = recentSprints
            .filter { it.id != null }
            .mapNotNull { sprint ->
                runCatching { getSprintMetrics(sprint.id!!) }.getOrNull()
            }

        return SprintTrends(
            sprints = sprintMetrics,
            avgVelocity = sprintMetrics.map { it.velocity }.average(),
            avgCompletionRate = sprintMetrics.map { it.completionRate }.average(),
            avgCycleTime = sprintMetrics.map { it.avgCycleTimeHours }.average(),
            avgCollaborationScore = sprintMetrics.map { it.collaborationScore }.average(),
            velocityTrend = calculateTrend(sprintMetrics.map { it.velocity.toDouble() }),
            completionRateTrend = calculateTrend(sprintMetrics.map { it.completionRate.toDouble() })
        )
    }

    // UTILITY METHODS

    /**
     * Calculates the trend direction based on a list of numerical values.
     *
     * The method divides the list into two halves, calculates the average for each half,
     * and determines the trend direction by comparing the averages.
     * If the second half's average is more than 10% greater than the first half's average,
     * it returns `IMPROVING`.
     * If the second half's average is more than 10% less than the first half's average,
     * it returns `DECLINING`.
     * Otherwise, it returns `STABLE`.
     *
     * @param values A list of `Double` values representing the data points to analyze.
     * @return A `TrendDirection` indicating whether the trend is improving, declining, or stable:
     *         - `IMPROVING` if the second half's average is more than 10% greater than the first half's average.
     *         - `DECLINING` if the second half's average is more than 10% less than the first half's average.
     *         - `STABLE` otherwise.
     */
    private fun calculateTrend(values: List<Double>): TrendDirection {
        if (values.size < 2) return TrendDirection.STABLE

        val firstHalf = values.take(values.size / 2).average()
        val secondHalf = values.drop(values.size / 2).average()

        return when {
            secondHalf > firstHalf * 1.1 -> TrendDirection.IMPROVING
            secondHalf < firstHalf * 0.9 -> TrendDirection.DECLINING
            else -> TrendDirection.STABLE
        }
    }

    private fun getEstimationMethod(sprintId: UUID): EstimationMethod {
        val sprint = ctx.sprint.findOneById(sprintId)
            ?: throw IllegalArgumentException("Sprint not found: $sprintId")

        val projectId = sprint.project.id
            ?: throw IllegalArgumentException("Sprint does not belong to a project: $sprintId")

        val project = ctx.project.findOneById(projectId)
            ?: throw ResourceNotFoundException("Project not found: $projectId")

        return project.estimationMethod
    }

    private fun getSprint(sprintId: UUID): SprintEntity {
        val sprint = ctx.sprint.findOneById(sprintId)
            ?: throw IllegalArgumentException("Sprint not found: $sprintId")
        return sprint
    }
}