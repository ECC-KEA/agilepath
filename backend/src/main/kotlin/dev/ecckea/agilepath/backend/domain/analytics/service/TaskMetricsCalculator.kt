package dev.ecckea.agilepath.backend.domain.analytics.service

import dev.ecckea.agilepath.backend.domain.analytics.model.TaskMetrics
import dev.ecckea.agilepath.backend.domain.sprint.model.SprintEventType
import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEntity
import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEventEntity
import dev.ecckea.agilepath.backend.domain.story.model.TaskEventType
import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskEntity
import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskEventEntity
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class TaskMetricsCalculator(
    private val ctx: RepositoryContext
) {
    fun calculateTaskMetrics(
        sprint: SprintEntity,
        tasks: List<TaskEntity>
    ): List<TaskMetrics> {
        if (tasks.isEmpty()) return emptyList()
        require(sprint.id != null) { throw IllegalArgumentException("Sprint ID cannot be null") }

        // Batch fetch all required data
        val taskIds = tasks.mapNotNull { it.id }
        val allTaskEvents = ctx.taskEvent.findAllByTaskIdIn(taskIds)
            .groupBy { it.taskId }
        val allSprintEvents = ctx.sprintEvent.findAllBySprintId(sprint.id!!)
        val subtaskCounts = countSubtasks(taskIds)

        // Get sprint timeline for context
        val sprintTimeline = getSprintTimeline(allSprintEvents)

        return tasks.mapNotNull { task ->
            val taskId = task.id ?: return@mapNotNull null
            val taskEvents = allTaskEvents[taskId] ?: emptyList()

            calculateSingleTaskMetrics(
                task = task,
                taskEvents = taskEvents,
                sprintTimeline = sprintTimeline,
                subtaskCount = subtaskCounts[taskId] ?: 0
            )
        }
    }

    private fun calculateSingleTaskMetrics(
        task: TaskEntity,
        taskEvents: List<TaskEventEntity>,
        sprintTimeline: SprintTimeline,
        subtaskCount: Int
    ): TaskMetrics {
        val sortedEvents = taskEvents.sortedBy { it.createdAt }

        return TaskMetrics(
            taskId = task.id!!,
            cycleTimeHours = calculateCycleTime(sortedEvents),
            wasReopened = wasTaskReopened(sortedEvents),
            wasAddedDuringSprint = wasTaskAddedDuringSprint(task, sprintTimeline),
            wasRemovedDuringSprint = wasTaskRemovedDuringSprint(sortedEvents, sprintTimeline),
            wasReassigned = wasTaskReassigned(sortedEvents),
            commentCount = calculateCommentCount(sortedEvents),
            isCompleted = isTaskCompleted(sortedEvents),
            subtaskCount = subtaskCount,
            timeToFirstComment = calculateTimeToFirstComment(sortedEvents),
            lastActivityTime = getLastActivityTime(sortedEvents),
            assignmentChanges = countAssignmentChanges(sortedEvents),
            statusTransitions = countStatusTransitions(sortedEvents)
        )
    }

    private fun countSubtasks(taskids: List<UUID>): Map<UUID, Int> {
        if (taskids.isEmpty()) return emptyMap()
        return ctx.subtask.findByTaskIdIn(taskids)
            .groupingBy { it.task.id!! } // Using !! to assert non-nullability is necessary here as the alternative would be to filter out nulls beforehand which is a O(n) operation
            .eachCount()
    }

    private fun calculateCycleTime(events: List<TaskEventEntity>): Double {
        val startEvent = events.firstOrNull { it.eventType == TaskEventType.STARTED }
        val completeEvent = events.lastOrNull { it.eventType == TaskEventType.COMPLETED }

        return if (startEvent != null && completeEvent != null) {
            val cycleTimeMs = completeEvent.createdAt.toEpochMilli() - startEvent.createdAt.toEpochMilli()
            cycleTimeMs / (1000.0 * 60.0 * 60.0) // Convert to hours
        } else 0.0
    }

    private fun wasTaskReopened(events: List<TaskEventEntity>): Boolean {
        return events.any { it.eventType == TaskEventType.REOPENED }
    }

    private fun wasTaskAddedDuringSprint(task: TaskEntity, timeline: SprintTimeline): Boolean {
        val taskCreatedAt = task.createdAt
        return timeline.sprintStarted != null &&
                timeline.sprintCompleted != null &&
                taskCreatedAt.isAfter(timeline.sprintStarted) &&
                taskCreatedAt.isBefore(timeline.sprintCompleted)
    }

    private fun wasTaskRemovedDuringSprint(events: List<TaskEventEntity>, timeline: SprintTimeline): Boolean {
        return timeline.sprintStarted != null &&
                timeline.sprintCompleted != null &&
                events.any { event ->
                    event.eventType == TaskEventType.DELETED &&
                            event.createdAt.isAfter(timeline.sprintStarted) &&
                            event.createdAt.isBefore(timeline.sprintCompleted)
                }
    }

    private fun wasTaskReassigned(events: List<TaskEventEntity>): Boolean {
        return events.any { it.eventType == TaskEventType.REASSIGNED }
    }

    private fun calculateCommentCount(events: List<TaskEventEntity>): Int {
        return events.count { it.eventType == TaskEventType.COMMENT_ADDED }
    }

    private fun isTaskCompleted(events: List<TaskEventEntity>): Boolean {
        return events.any { it.eventType == TaskEventType.COMPLETED }
    }

    private fun calculateTimeToFirstComment(events: List<TaskEventEntity>): Double? {
        val createdEvent = events.firstOrNull { it.eventType == TaskEventType.CREATED }
        val firstComment = events.firstOrNull { it.eventType == TaskEventType.COMMENT_ADDED }

        return if (createdEvent != null && firstComment != null) {
            val timeMs = firstComment.createdAt.toEpochMilli() - createdEvent.createdAt.toEpochMilli()
            timeMs / (1000.0 * 60.0 * 60.0) // Convert to hours
        } else null
    }

    private fun getLastActivityTime(events: List<TaskEventEntity>): Instant? {
        return events.maxByOrNull { it.createdAt }?.createdAt
    }

    private fun countAssignmentChanges(events: List<TaskEventEntity>): Int {
        return events.count {
            it.eventType in setOf(
                TaskEventType.ASSIGNED,
                TaskEventType.UNASSIGNED,
                TaskEventType.REASSIGNED
            )
        }
    }

    private fun countStatusTransitions(events: List<TaskEventEntity>): Int {
        return events.count {
            it.eventType in setOf(
                TaskEventType.STARTED,
                TaskEventType.COMPLETED,
                TaskEventType.REOPENED,
            )
        }
    }

    private fun getSprintTimeline(sprintEvents: List<SprintEventEntity>): SprintTimeline {
        val sortedEvents = sprintEvents.sortedBy { it.createdAt }
        return SprintTimeline(
            sprintStarted = sortedEvents.firstOrNull { it.eventType == SprintEventType.STARTED }?.createdAt,
            sprintCompleted = sortedEvents.lastOrNull { it.eventType == SprintEventType.COMPLETED }?.createdAt
        )
    }

    private data class SprintTimeline(
        val sprintStarted: Instant?,
        val sprintCompleted: Instant?
    )
}