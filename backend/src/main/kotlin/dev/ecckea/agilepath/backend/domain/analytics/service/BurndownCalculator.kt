package dev.ecckea.agilepath.backend.domain.analytics.service

import dev.ecckea.agilepath.backend.domain.analytics.model.BurndownData
import dev.ecckea.agilepath.backend.domain.analytics.model.BurndownPoint
import dev.ecckea.agilepath.backend.domain.project.model.EstimationMethod
import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEntity
import dev.ecckea.agilepath.backend.domain.story.model.TaskEventType
import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskEntity
import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskEventEntity
import dev.ecckea.agilepath.backend.shared.utils.toLocalDate
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
class BurndownCalculator {

    /**
     * Calculate burndown chart data with an optimal path and actual progress
     */
    fun calculateBurndown(
        sprint: SprintEntity,
        tasks: List<TaskEntity>,
        taskEvents: List<TaskEventEntity>,
        estimationMethod: EstimationMethod
    ): BurndownData {
        require(sprint.id != null) { "Sprint ID cannot be null" }

        val totalWork = calculateTotalPoints(tasks, estimationMethod)
        val optimalPath = generateOptimalPath(sprint, totalWork)
        val actualProgress = generateActualProgress(sprint, tasks, taskEvents, estimationMethod)

        return BurndownData(
            sprintId = sprint.id!!,
            totalWork = totalWork,
            optimalPath = optimalPath,
            actualProgress = actualProgress,
            startDate = sprint.startDate,
            endDate = sprint.endDate
        )
    }

    /**
     * Generate ideal linear burndown from start to finish
     */
    private fun generateOptimalPath(sprint: SprintEntity, totalWork: Int): List<BurndownPoint> {
        val totalDays = ChronoUnit.DAYS.between(sprint.startDate, sprint.endDate).toInt()

        if (totalDays <= 0) return listOf(BurndownPoint(sprint.startDate, totalWork.toDouble()))

        val dailyBurnRate = totalWork.toDouble() / totalDays

        return (0..totalDays).map { dayOffset ->
            val date = sprint.startDate.plusDays(dayOffset.toLong())
            val remaining = totalWork - (dailyBurnRate * dayOffset)
            BurndownPoint(date, maxOf(0.0, remaining))
        }
    }

    /**
     * Calculate actual progress based on task completion events
     */
    private fun generateActualProgress(
        sprint: SprintEntity,
        tasks: List<TaskEntity>,
        taskEvents: List<TaskEventEntity>,
        estimationMethod: EstimationMethod
    ): List<BurndownPoint> {
        val totalWork = calculateTotalPoints(tasks, estimationMethod).toDouble()

        // Group completion events by date
        val completionsByDate = taskEvents
            .filter { it.eventType == TaskEventType.COMPLETED }
            .groupBy { toLocalDate(it.createdAt) }
            .toSortedMap()

        // Map tasks to their point values
        val taskPoints = tasks.associate { task ->
            task.id to when (estimationMethod) {
                EstimationMethod.STORY_POINTS -> task.estimatePoints?.points ?: 0
                EstimationMethod.TSHIRT_SIZES -> task.estimateTshirt?.points ?: 0
            }
        }

        val progress = mutableListOf<BurndownPoint>()
        var remainingWork = totalWork

        // Start with full work remaining
        progress.add(BurndownPoint(sprint.startDate, totalWork))

        // Calculate daily progress through sprint (or until today, whichever is earlier)
        var currentDate = sprint.startDate.plusDays(1)
        val endDate = minOf(sprint.endDate, LocalDate.now())

        while (!currentDate.isAfter(endDate)) {
            // Sum points completed on this date
            val completionsToday = completionsByDate[currentDate] ?: emptyList()
            val pointsCompleted = completionsToday.sumOf { event ->
                taskPoints[event.taskId] ?: 0
            }

            remainingWork -= pointsCompleted
            progress.add(BurndownPoint(currentDate, maxOf(0.0, remainingWork)))

            currentDate = currentDate.plusDays(1)
        }

        return progress
    }

    /**
     * Calculate total story points or t-shirt size points
     */
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
}