package dev.ecckea.agilepath.backend.domain.analytics.service

import dev.ecckea.agilepath.backend.domain.analytics.model.SprintInsight
import dev.ecckea.agilepath.backend.domain.analytics.model.SprintMetrics
import dev.ecckea.agilepath.backend.domain.analytics.model.TaskComplexityBreakdown
import dev.ecckea.agilepath.backend.domain.analytics.model.TaskMetrics
import dev.ecckea.agilepath.backend.domain.column.model.SprintColumnStatus
import dev.ecckea.agilepath.backend.domain.column.repository.entity.SprintColumnEntity
import dev.ecckea.agilepath.backend.domain.project.model.EstimationMethod
import dev.ecckea.agilepath.backend.domain.project.model.Framework
import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEntity
import dev.ecckea.agilepath.backend.domain.sprint.repository.SprintRepository
import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEntity
import dev.ecckea.agilepath.backend.domain.story.model.PointEstimate
import dev.ecckea.agilepath.backend.domain.story.model.TshirtEstimate
import dev.ecckea.agilepath.backend.domain.story.repository.TaskRepository
import dev.ecckea.agilepath.backend.domain.story.repository.entity.StoryEntity
import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskEntity
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.util.*

class SprintMetricsCalculatorUnitTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var sprintRepository: SprintRepository
    private lateinit var repositoryContext: RepositoryContext
    private lateinit var taskMetricsCalculator: TaskMetricsCalculator
    private lateinit var calculator: SprintMetricsCalculator

    @BeforeEach
    fun setUp() {
        taskRepository = mockk()
        sprintRepository = mockk()
        repositoryContext = mockk()
        taskMetricsCalculator = mockk()

        every { repositoryContext.task } returns taskRepository
        every { repositoryContext.sprint } returns sprintRepository

        calculator = SprintMetricsCalculator(repositoryContext, taskMetricsCalculator)
    }

    @Test
    fun `calculateSprintMetrics throws exception for null sprint ID`() {
        val sprint = createSprintEntity(null)
        val tasks = listOf(createTaskEntity())
        val taskMetrics = emptyList<TaskMetrics>()

        assertThrows(IllegalArgumentException::class.java) {
            calculator.calculateSprintMetrics(sprint, tasks, taskMetrics, EstimationMethod.STORY_POINTS)
        }
    }

    @Test
    fun `calculateSprintMetrics returns correct metrics for completed sprint`() {
        val sprintId = UUID.randomUUID()
        val sprint = createSprintEntity(
            sprintId,
            startDate = LocalDate.of(2023, 1, 1),
            endDate = LocalDate.of(2023, 1, 14)
        )

        val tasks = listOf(
            createTaskEntity(estimatePoints = PointEstimate.POINT_5),
            createTaskEntity(estimatePoints = PointEstimate.POINT_3)
        )

        val taskMetrics = listOf(
            createTaskMetrics(
                taskId = tasks[0].id!!,
                isCompleted = true,
                cycleTimeHours = 24.0,
                wasReopened = false,
                commentCount = 2,
                subtaskCount = 1
            ),
            createTaskMetrics(
                taskId = tasks[1].id!!,
                isCompleted = true,
                cycleTimeHours = 16.0,
                wasReopened = true,
                commentCount = 0,
                subtaskCount = 3
            )
        )

        val result = calculator.calculateSprintMetrics(sprint, tasks, taskMetrics, EstimationMethod.STORY_POINTS)

        assertEquals(sprintId, result.sprintId)
        assertEquals(8, result.velocity) // 5 + 3 points
        assertEquals(14, result.durationDays) // 14 days inclusive
        assertEquals(20.0, result.avgCycleTimeHours, 0.01) // (24 + 16) / 2
        assertEquals(tasks[0].id, result.longestCycleTimeTaskId) // Task with 24h cycle time
        assertEquals(1, result.numReopenedTasks)
        assertEquals(0, result.numAddedTasksDuringSprint)
        assertEquals(0, result.numRemovedTasksDuringSprint)
        assertEquals(0, result.numReassignedTasks)
        assertEquals(1, result.numTasksWithComments) // Only first task has comments
        assertEquals(1.0, result.avgTaskCommentCount, 0.01) // (2 + 0) / 2
        assertEquals(2.0, result.avgSubtaskCount, 0.01) // (1 + 3) / 2
        assertEquals(100, result.completionRate) // 2/2 * 100
        assertEquals(1, result.tasksWithoutComments) // Second task
    }

    @Test
    fun `calculateSprintMetrics handles empty task list`() {
        val sprintId = UUID.randomUUID()
        val sprint = createSprintEntity(sprintId)
        val tasks = emptyList<TaskEntity>()
        val taskMetrics = emptyList<TaskMetrics>()

        val result = calculator.calculateSprintMetrics(sprint, tasks, taskMetrics, EstimationMethod.STORY_POINTS)

        assertEquals(sprintId, result.sprintId)
        assertEquals(0, result.velocity)
        assertEquals(0, result.completionRate)
        assertEquals(0.0, result.avgCycleTimeHours)
        assertEquals(0.0, result.avgTaskCommentCount)
        assertEquals(0.0, result.avgSubtaskCount)
        assertNull(result.longestCycleTimeTaskId)
    }

    @Test
    fun `calculateSprintMetrics with T-shirt sizing`() {
        val sprintId = UUID.randomUUID()
        val sprint = createSprintEntity(sprintId)

        val tasks = listOf(
            createTaskEntity(estimateTshirt = TshirtEstimate.LARGE),
            createTaskEntity(estimateTshirt = TshirtEstimate.MEDIUM)
        )

        val taskMetrics = listOf(
            createTaskMetrics(taskId = tasks[0].id!!, isCompleted = true),
            createTaskMetrics(taskId = tasks[1].id!!, isCompleted = false)
        )

        val result = calculator.calculateSprintMetrics(sprint, tasks, taskMetrics, EstimationMethod.TSHIRT_SIZES)

        assertEquals(5, result.velocity) // Only LARGE (5 points) completed
        assertEquals(50, result.completionRate) // 1/2 * 100
    }

    @Test
    fun `calculateSprintMetadata returns correct metadata`() {
        val sprintId = UUID.randomUUID()
        val sprint = createSprintEntity(sprintId, teamCapacity = 15)

        val tasks = listOf(
            createTaskEntity(estimatePoints = PointEstimate.POINT_5),
            createTaskEntity(estimatePoints = PointEstimate.POINT_3),
            createTaskEntity(estimatePoints = PointEstimate.POINT_2)
        )

        every { taskRepository.countCompletedTasksForSprint(sprintId) } returns 2

        val result = calculator.calculateSprintMetadata(sprint, tasks, EstimationMethod.STORY_POINTS)

        assertEquals(sprintId, result.sprintId)
        assertEquals(15, result.teamCapacity)
        assertEquals(3, result.totalTasks)
        assertEquals(2, result.completedTasks)
        assertEquals(10, result.plannedPoints) // 5 + 3 + 2
    }

    @Test
    fun `calculateSprintMetadata with T-shirt sizes`() {
        val sprintId = UUID.randomUUID()
        val sprint = createSprintEntity(sprintId)

        val tasks = listOf(
            createTaskEntity(estimateTshirt = TshirtEstimate.XLARGE),
            createTaskEntity(estimateTshirt = TshirtEstimate.SMALL)
        )

        every { taskRepository.countCompletedTasksForSprint(sprintId) } returns 1

        val result = calculator.calculateSprintMetadata(sprint, tasks, EstimationMethod.TSHIRT_SIZES)

        assertEquals(10, result.plannedPoints) // 8 + 2
    }

    @Test
    fun `calculateVelocity with story points`() {
        val tasks = listOf(
            createTaskEntity(id = UUID.randomUUID(), estimatePoints = PointEstimate.POINT_8),
            createTaskEntity(id = UUID.randomUUID(), estimatePoints = PointEstimate.POINT_3),
            createTaskEntity(id = UUID.randomUUID(), estimatePoints = PointEstimate.POINT_5)
        )

        val completedTasks = listOf(
            createTaskMetrics(taskId = tasks[0].id!!, isCompleted = true),
            createTaskMetrics(taskId = tasks[2].id!!, isCompleted = true)
        )

        val velocity = calculateVelocityReflection(completedTasks, tasks, EstimationMethod.STORY_POINTS)
        assertEquals(13, velocity) // 8 + 5
    }

    @Test
    fun `calculateVelocity with T-shirt sizes`() {
        val tasks = listOf(
            createTaskEntity(id = UUID.randomUUID(), estimateTshirt = TshirtEstimate.LARGE),
            createTaskEntity(id = UUID.randomUUID(), estimateTshirt = TshirtEstimate.MEDIUM)
        )

        val completedTasks = listOf(
            createTaskMetrics(taskId = tasks[0].id!!, isCompleted = true)
        )

        val velocity = calculateVelocityReflection(completedTasks, tasks, EstimationMethod.TSHIRT_SIZES)
        assertEquals(5, velocity) // Only LARGE completed
    }

    @Test
    fun `calculateSprintDuration returns correct days`() {
        val sprint = createSprintEntity(
            startDate = LocalDate.of(2023, 1, 1),
            endDate = LocalDate.of(2023, 1, 7)
        )

        val duration = calculateSprintDurationReflection(sprint)
        assertEquals(7, duration) // 7 days inclusive
    }

    @Test
    fun `calculateAverageCycleTime handles empty and zero values`() {
        val completedTasks = listOf(
            createTaskMetrics(cycleTimeHours = 0.0),
            createTaskMetrics(cycleTimeHours = 24.0),
            createTaskMetrics(cycleTimeHours = 16.0)
        )

        val avgCycleTime = calculateAverageCycleTimeReflection(completedTasks)
        assertEquals(20.0, avgCycleTime, 0.01) // (24 + 16) / 2, ignoring 0
    }

    @Test
    fun `categorizeTasksByComplexity returns correct breakdown`() {
        val taskMetrics = listOf(
            createTaskMetrics(subtaskCount = 1, commentCount = 1),
            createTaskMetrics(subtaskCount = 3, commentCount = 4),
            createTaskMetrics(subtaskCount = 6, commentCount = 2),
            createTaskMetrics(subtaskCount = 2, commentCount = 8),
            createTaskMetrics(subtaskCount = 0, commentCount = 0)
        )

        val breakdown = categorizeTasksByComplexityReflection(taskMetrics)
        assertEquals(2, breakdown.simple)
        assertEquals(1, breakdown.moderate)
        assertEquals(2, breakdown.complex)
    }

    @Test
    fun `calculateCollaborationScore returns correct score`() {
        val taskMetrics = listOf(
            createTaskMetrics(commentCount = 3, assignmentChanges = 1),
            createTaskMetrics(commentCount = 0, assignmentChanges = 1),
            createTaskMetrics(commentCount = 2, assignmentChanges = 3), // High reassignments
            createTaskMetrics(commentCount = 1, assignmentChanges = 1)
        )

        val score = calculateCollaborationScoreReflection(taskMetrics)
        assertTrue(score > 0)
        assertTrue(score <= 100)
        // Score should be: 75% tasks with comments * 50 + engagement - 25% reassignment penalty
        // = 37.5 + engagement - 5 = ~32.5 + engagement
    }

    @Test
    fun `calculateCollaborationScore handles empty list`() {
        val emptyTaskMetrics = emptyList<TaskMetrics>()
        val score = calculateCollaborationScoreReflection(emptyTaskMetrics)
        assertEquals(0.0, score)
    }

    @Test
    fun `generateInsights creates appropriate insights`() {
        val sprintMetrics = SprintMetrics(
            sprintId = UUID.randomUUID(),
            velocity = 10,
            durationDays = 14,
            avgCycleTimeHours = 20.0,
            longestCycleTimeTaskId = UUID.randomUUID(),
            numReopenedTasks = 3, // High for 10 tasks
            numAddedTasksDuringSprint = 2,
            numRemovedTasksDuringSprint = 0,
            numReassignedTasks = 4, // High for 10 tasks
            numTasksWithComments = 5,
            avgTaskCommentCount = 2.5,
            avgSubtaskCount = 2.0,
            totalAssignmentChanges = 8,
            totalStatusTransitions = 15,
            tasksWithoutComments = 5,
            completionRate = 60, // Low completion rate
            tasksByComplexity = TaskComplexityBreakdown(3, 4, 3),
            collaborationScore = 25.0 // Low collaboration
        )

        val taskMetrics = (1..10).map { createTaskMetrics() } // 10 tasks for percentage calculations

        val insights = generateInsightsReflection(sprintMetrics, taskMetrics)

        assertTrue(insights.any { it is SprintInsight.LowCompletionRate })
        assertTrue(insights.any { it is SprintInsight.HighReopenRate })
        assertTrue(insights.any { it is SprintInsight.LowCollaboration })
        assertTrue(insights.any { it is SprintInsight.FrequentReassignments })
        assertTrue(insights.any { it is SprintInsight.ScopeIncrease })
    }

    @Test
    fun `generateInsights handles good sprint metrics`() {
        val sprintMetrics = SprintMetrics(
            sprintId = UUID.randomUUID(),
            velocity = 15,
            durationDays = 14,
            avgCycleTimeHours = 20.0,
            longestCycleTimeTaskId = UUID.randomUUID(),
            numReopenedTasks = 1, // Low
            numAddedTasksDuringSprint = 0, // None added
            numRemovedTasksDuringSprint = 0,
            numReassignedTasks = 1, // Low
            numTasksWithComments = 8,
            avgTaskCommentCount = 3.0,
            avgSubtaskCount = 2.5,
            totalAssignmentChanges = 10,
            totalStatusTransitions = 20,
            tasksWithoutComments = 2,
            completionRate = 90, // High completion
            tasksByComplexity = TaskComplexityBreakdown(4, 4, 2),
            collaborationScore = 75.0 // Good collaboration
        )

        val taskMetrics = (1..10).map { createTaskMetrics() }

        val insights = generateInsightsReflection(sprintMetrics, taskMetrics)

        // Should have no negative insights
        assertTrue(insights.none { it is SprintInsight.LowCompletionRate })
        assertTrue(insights.none { it is SprintInsight.HighReopenRate })
        assertTrue(insights.none { it is SprintInsight.LowCollaboration })
        assertTrue(insights.none { it is SprintInsight.FrequentReassignments })
        assertTrue(insights.none { it is SprintInsight.ScopeIncrease })
    }

    // Helper methods for creating test entities
    private fun createUserEntity(id: String = "test-user-id") = UserEntity(
        id = id,
        email = "test@example.com",
        githubUsername = "testuser",
        fullName = "Test User",
        createdAt = Instant.parse("2023-01-01T09:00:00Z")
    )

    private fun createProjectEntity(id: UUID = UUID.randomUUID()) = ProjectEntity(
        id = id,
        name = "Test Project",
        description = "Test Description",
        framework = Framework.SCRUM,
        estimationMethod = EstimationMethod.STORY_POINTS,
        createdBy = createUserEntity(),
        createdAt = Instant.parse("2023-01-01T09:00:00Z")
    )

    private fun createSprintEntity(
        id: UUID? = UUID.randomUUID(),
        teamCapacity: Int = 10,
        startDate: LocalDate = LocalDate.of(2023, 1, 1),
        endDate: LocalDate = LocalDate.of(2023, 1, 14)
    ) = SprintEntity(
        id = id,
        project = createProjectEntity(),
        name = "Test Sprint",
        goal = "Test Goal",
        teamCapacity = teamCapacity,
        startDate = startDate,
        endDate = endDate,
        createdBy = createUserEntity(),
        createdAt = Instant.parse("2023-01-01T09:00:00Z")
    )

    private fun createSprintColumnEntity() = SprintColumnEntity(
        id = UUID.randomUUID(),
        sprint = createSprintEntity(),
        name = "To Do",
        status = SprintColumnStatus.TODO,
        columnIndex = 0
    )

    private fun createStoryEntity() = StoryEntity(
        id = UUID.randomUUID(),
        project = createProjectEntity(),
        title = "Test Story",
        description = "Test Description",
        status = "TODO",
        priority = 1,
        createdBy = createUserEntity(),
        createdAt = Instant.parse("2023-01-01T09:00:00Z")
    )

    private fun createTaskEntity(
        id: UUID = UUID.randomUUID(),
        estimatePoints: PointEstimate? = null,
        estimateTshirt: TshirtEstimate? = null,
        createdAt: Instant = Instant.parse("2023-01-01T09:00:00Z")
    ) = TaskEntity(
        id = id,
        story = createStoryEntity(),
        sprintColumn = createSprintColumnEntity(),
        title = "Test Task",
        description = "Test Description",
        estimatePoints = estimatePoints,
        estimateTshirt = estimateTshirt,
        createdBy = createUserEntity(),
        createdAt = createdAt
    )

    private fun createTaskMetrics(
        taskId: UUID = UUID.randomUUID(),
        cycleTimeHours: Double = 0.0,
        wasReopened: Boolean = false,
        wasAddedDuringSprint: Boolean = false,
        wasRemovedDuringSprint: Boolean = false,
        wasReassigned: Boolean = false,
        commentCount: Int = 0,
        isCompleted: Boolean = false,
        subtaskCount: Int = 0,
        timeToFirstComment: Double? = null,
        lastActivityTime: Instant? = null,
        assignmentChanges: Int = 0,
        statusTransitions: Int = 0
    ) = TaskMetrics(
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

    // Reflection helper methods to test private methods
    private fun calculateVelocityReflection(
        completedTasks: List<TaskMetrics>,
        allTasks: List<TaskEntity>,
        estimationMethod: EstimationMethod
    ): Int {
        val method = calculator.javaClass.getDeclaredMethod(
            "calculateVelocity",
            List::class.java,
            List::class.java,
            EstimationMethod::class.java
        )
        method.isAccessible = true
        return method.invoke(calculator, completedTasks, allTasks, estimationMethod) as Int
    }

    private fun calculateSprintDurationReflection(sprint: SprintEntity): Int {
        val method = calculator.javaClass.getDeclaredMethod("calculateSprintDuration", SprintEntity::class.java)
        method.isAccessible = true
        return method.invoke(calculator, sprint) as Int
    }

    private fun calculateAverageCycleTimeReflection(completedTasks: List<TaskMetrics>): Double {
        val method = calculator.javaClass.getDeclaredMethod("calculateAverageCycleTime", List::class.java)
        method.isAccessible = true
        return method.invoke(calculator, completedTasks) as Double
    }

    private fun categorizeTasksByComplexityReflection(taskMetrics: List<TaskMetrics>): TaskComplexityBreakdown {
        val method = calculator.javaClass.getDeclaredMethod("categorizeTasksByComplexity", List::class.java)
        method.isAccessible = true
        return method.invoke(calculator, taskMetrics) as TaskComplexityBreakdown
    }

    private fun calculateCollaborationScoreReflection(taskMetrics: List<TaskMetrics>): Double {
        val method = calculator.javaClass.getDeclaredMethod("calculateCollaborationScore", List::class.java)
        method.isAccessible = true
        return method.invoke(calculator, taskMetrics) as Double
    }

    private fun generateInsightsReflection(
        sprintMetrics: SprintMetrics,
        taskMetrics: List<TaskMetrics>
    ): List<SprintInsight> {
        val method = calculator.javaClass.getDeclaredMethod(
            "generateInsights",
            SprintMetrics::class.java,
            List::class.java
        )
        method.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        return method.invoke(calculator, sprintMetrics, taskMetrics) as List<SprintInsight>
    }
}