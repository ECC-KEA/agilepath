package dev.ecckea.agilepath.backend.domain.analytics.service

import dev.ecckea.agilepath.backend.domain.column.model.SprintColumnStatus
import dev.ecckea.agilepath.backend.domain.column.repository.entity.SprintColumnEntity
import dev.ecckea.agilepath.backend.domain.project.model.EstimationMethod
import dev.ecckea.agilepath.backend.domain.project.model.Framework
import dev.ecckea.agilepath.backend.domain.project.repository.entity.ProjectEntity
import dev.ecckea.agilepath.backend.domain.sprint.model.SprintEventType
import dev.ecckea.agilepath.backend.domain.sprint.repository.SprintEventRepository
import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEntity
import dev.ecckea.agilepath.backend.domain.sprint.repository.entity.SprintEventEntity
import dev.ecckea.agilepath.backend.domain.story.model.TaskEventType
import dev.ecckea.agilepath.backend.domain.story.repository.SubtaskRepository
import dev.ecckea.agilepath.backend.domain.story.repository.TaskEventRepository
import dev.ecckea.agilepath.backend.domain.story.repository.entity.StoryEntity
import dev.ecckea.agilepath.backend.domain.story.repository.entity.SubtaskEntity
import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskEntity
import dev.ecckea.agilepath.backend.domain.story.repository.entity.TaskEventEntity
import dev.ecckea.agilepath.backend.domain.user.repository.entity.UserEntity
import dev.ecckea.agilepath.backend.shared.context.repository.RepositoryContext
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class TaskMetricsCalculatorUnitTest {

    private lateinit var taskEventRepository: TaskEventRepository
    private lateinit var sprintEventRepository: SprintEventRepository
    private lateinit var subtaskRepository: SubtaskRepository
    private lateinit var repositoryContext: RepositoryContext
    private lateinit var calculator: TaskMetricsCalculator

    @BeforeEach
    fun setUp() {
        taskEventRepository = mockk()
        sprintEventRepository = mockk()
        subtaskRepository = mockk()
        repositoryContext = mockk()

        every { repositoryContext.taskEvent } returns taskEventRepository
        every { repositoryContext.sprintEvent } returns sprintEventRepository
        every { repositoryContext.subtask } returns subtaskRepository

        calculator = TaskMetricsCalculator(repositoryContext)
    }

    @Test
    fun `calculateTaskMetrics returns empty list for empty tasks`() {
        val sprint = createSprintEntity()
        val result = calculator.calculateTaskMetrics(sprint, emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun `calculateTaskMetrics returns metrics for valid tasks`() {
        val sprintId = UUID.randomUUID()
        val taskId = UUID.randomUUID()
        val sprint = createSprintEntity(sprintId)
        val task = createTaskEntity(taskId)

        val taskEvents = listOf(
            createTaskEvent(taskId, TaskEventType.CREATED, "2023-01-01T10:00:00Z"),
            createTaskEvent(taskId, TaskEventType.STARTED, "2023-01-01T11:00:00Z"),
            createTaskEvent(taskId, TaskEventType.COMPLETED, "2023-01-01T15:00:00Z")
        )

        val sprintEvents = listOf(
            createSprintEvent(sprintId, SprintEventType.STARTED, "2023-01-01T09:00:00Z"),
            createSprintEvent(sprintId, SprintEventType.COMPLETED, "2023-01-01T18:00:00Z")
        )

        every { taskEventRepository.findAllByTaskIdIn(listOf(taskId)) } returns taskEvents
        every { sprintEventRepository.findAllBySprintId(sprintId) } returns sprintEvents
        every { subtaskRepository.findByTaskIdIn(listOf(taskId)) } returns emptyList()

        val result = calculator.calculateTaskMetrics(sprint, listOf(task))

        assertEquals(1, result.size)
        val metrics = result.first()
        assertEquals(taskId, metrics.taskId)
        assertEquals(4.0, metrics.cycleTimeHours, 0.01) // 4 hours from start to completion
        assertFalse(metrics.wasReopened)
        assertFalse(metrics.wasAddedDuringSprint) // Task created before sprint
        assertFalse(metrics.wasRemovedDuringSprint)
        assertFalse(metrics.wasReassigned)
        assertEquals(0, metrics.commentCount)
        assertTrue(metrics.isCompleted)
        assertEquals(0, metrics.subtaskCount)
        assertNull(metrics.timeToFirstComment) // No comments
        assertNotNull(metrics.lastActivityTime)
    }

    @Test
    fun `cycleTime calculation returns zero when no start or complete events`() {
        val taskEvents = listOf(
            createTaskEvent(UUID.randomUUID(), TaskEventType.CREATED, "2023-01-01T10:00:00Z")
        )

        // Using reflection to test private method
        val cycleTime = calculateCycleTimeReflection(taskEvents)
        assertEquals(0.0, cycleTime)
    }

    @Test
    fun `cycleTime calculation returns correct hours between start and completion`() {
        val taskEvents = listOf(
            createTaskEvent(UUID.randomUUID(), TaskEventType.STARTED, "2023-01-01T10:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.COMPLETED, "2023-01-01T16:30:00Z")
        )

        val cycleTime = calculateCycleTimeReflection(taskEvents)
        assertEquals(6.5, cycleTime, 0.01) // 6.5 hours
    }

    @Test
    fun `wasTaskReopened returns true when reopened event exists`() {
        val taskEvents = listOf(
            createTaskEvent(UUID.randomUUID(), TaskEventType.CREATED, "2023-01-01T10:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.REOPENED, "2023-01-01T11:00:00Z")
        )

        val wasReopened = wasTaskReopenedReflection(taskEvents)
        assertTrue(wasReopened)
    }

    @Test
    fun `wasTaskReopened returns false when no reopened event`() {
        val taskEvents = listOf(
            createTaskEvent(UUID.randomUUID(), TaskEventType.CREATED, "2023-01-01T10:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.COMPLETED, "2023-01-01T11:00:00Z")
        )

        val wasReopened = wasTaskReopenedReflection(taskEvents)
        assertFalse(wasReopened)
    }

    @Test
    fun `wasTaskAddedDuringSprint returns true when task created during sprint`() {
        val task = createTaskEntity(createdAt = Instant.parse("2023-01-01T12:00:00Z"))
        val timeline = createSprintTimeline(
            started = Instant.parse("2023-01-01T10:00:00Z"),
            completed = Instant.parse("2023-01-01T18:00:00Z")
        )

        val wasAdded = wasTaskAddedDuringSprintReflection(task, timeline)
        assertTrue(wasAdded)
    }

    @Test
    fun `wasTaskAddedDuringSprint returns false when task created before sprint`() {
        val task = createTaskEntity(createdAt = Instant.parse("2023-01-01T08:00:00Z"))
        val timeline = createSprintTimeline(
            started = Instant.parse("2023-01-01T10:00:00Z"),
            completed = Instant.parse("2023-01-01T18:00:00Z")
        )

        val wasAdded = wasTaskAddedDuringSprintReflection(task, timeline)
        assertFalse(wasAdded)
    }

    @Test
    fun `wasTaskRemovedDuringSprint returns true when task deleted during sprint`() {
        val taskEvents = listOf(
            createTaskEvent(UUID.randomUUID(), TaskEventType.CREATED, "2023-01-01T10:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.DELETED, "2023-01-01T14:00:00Z")
        )
        val timeline = createSprintTimeline(
            started = Instant.parse("2023-01-01T10:00:00Z"),
            completed = Instant.parse("2023-01-01T18:00:00Z")
        )

        val wasRemoved = wasTaskRemovedDuringSprintReflection(taskEvents, timeline)
        assertTrue(wasRemoved)
    }

    @Test
    fun `wasTaskRemovedDuringSprint returns false when no deletion during sprint`() {
        val taskEvents = listOf(
            createTaskEvent(UUID.randomUUID(), TaskEventType.CREATED, "2023-01-01T10:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.COMPLETED, "2023-01-01T14:00:00Z")
        )
        val timeline = createSprintTimeline(
            started = Instant.parse("2023-01-01T10:00:00Z"),
            completed = Instant.parse("2023-01-01T18:00:00Z")
        )

        val wasRemoved = wasTaskRemovedDuringSprintReflection(taskEvents, timeline)
        assertFalse(wasRemoved)
    }

    @Test
    fun `wasTaskReassigned returns true when reassigned event exists`() {
        val taskEvents = listOf(
            createTaskEvent(UUID.randomUUID(), TaskEventType.ASSIGNED, "2023-01-01T10:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.REASSIGNED, "2023-01-01T11:00:00Z")
        )

        val wasReassigned = wasTaskReassignedReflection(taskEvents)
        assertTrue(wasReassigned)
    }

    @Test
    fun `calculateCommentCount returns correct count`() {
        val taskEvents = listOf(
            createTaskEvent(UUID.randomUUID(), TaskEventType.CREATED, "2023-01-01T10:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.COMMENT_ADDED, "2023-01-01T11:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.COMMENT_ADDED, "2023-01-01T12:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.COMPLETED, "2023-01-01T13:00:00Z")
        )

        val commentCount = calculateCommentCountReflection(taskEvents)
        assertEquals(2, commentCount)
    }

    @Test
    fun `isTaskCompleted returns true when completed event exists`() {
        val taskEvents = listOf(
            createTaskEvent(UUID.randomUUID(), TaskEventType.CREATED, "2023-01-01T10:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.COMPLETED, "2023-01-01T11:00:00Z")
        )

        val isCompleted = isTaskCompletedReflection(taskEvents)
        assertTrue(isCompleted)
    }

    @Test
    fun `calculateTimeToFirstComment returns correct time in hours`() {
        val taskEvents = listOf(
            createTaskEvent(UUID.randomUUID(), TaskEventType.CREATED, "2023-01-01T10:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.COMMENT_ADDED, "2023-01-01T12:30:00Z")
        )

        val timeToFirstComment = calculateTimeToFirstCommentReflection(taskEvents)
        assertNotNull(timeToFirstComment)
        assertEquals(2.5, timeToFirstComment!!, 0.01) // 2.5 hours
    }

    @Test
    fun `calculateTimeToFirstComment returns null when no comments`() {
        val taskEvents = listOf(
            createTaskEvent(UUID.randomUUID(), TaskEventType.CREATED, "2023-01-01T10:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.COMPLETED, "2023-01-01T11:00:00Z")
        )

        val timeToFirstComment = calculateTimeToFirstCommentReflection(taskEvents)
        assertNull(timeToFirstComment)
    }

    @Test
    fun `getLastActivityTime returns latest event time`() {
        val taskEvents = listOf(
            createTaskEvent(UUID.randomUUID(), TaskEventType.CREATED, "2023-01-01T10:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.STARTED, "2023-01-01T11:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.COMPLETED, "2023-01-01T15:00:00Z")
        )

        val lastActivity = getLastActivityTimeReflection(taskEvents)
        assertNotNull(lastActivity)
        assertEquals(Instant.parse("2023-01-01T15:00:00Z"), lastActivity)
    }

    @Test
    fun `countAssignmentChanges returns correct count`() {
        val taskEvents = listOf(
            createTaskEvent(UUID.randomUUID(), TaskEventType.CREATED, "2023-01-01T10:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.ASSIGNED, "2023-01-01T11:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.REASSIGNED, "2023-01-01T12:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.UNASSIGNED, "2023-01-01T13:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.COMPLETED, "2023-01-01T14:00:00Z")
        )

        val assignmentChanges = countAssignmentChangesReflection(taskEvents)
        assertEquals(3, assignmentChanges) // ASSIGNED, REASSIGNED, UNASSIGNED
    }

    @Test
    fun `countStatusTransitions returns correct count`() {
        val taskEvents = listOf(
            createTaskEvent(UUID.randomUUID(), TaskEventType.CREATED, "2023-01-01T10:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.STARTED, "2023-01-01T11:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.COMPLETED, "2023-01-01T12:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.REOPENED, "2023-01-01T13:00:00Z"),
            createTaskEvent(UUID.randomUUID(), TaskEventType.COMMENT_ADDED, "2023-01-01T14:00:00Z")
        )

        val statusTransitions = countStatusTransitionsReflection(taskEvents)
        assertEquals(3, statusTransitions) // STARTED, COMPLETED, REOPENED
    }

    @Test
    fun `countSubtasks returns correct mapping`() {
        val taskId1 = UUID.randomUUID()
        val taskId2 = UUID.randomUUID()
        val task1 = createTaskEntity(taskId1)
        val task2 = createTaskEntity(taskId2)

        val subtasks = listOf(
            createSubtaskEntity(task1),
            createSubtaskEntity(task1),
            createSubtaskEntity(task2)
        )

        every { subtaskRepository.findByTaskIdIn(listOf(taskId1, taskId2)) } returns subtasks

        val result = countSubtasksReflection(listOf(taskId1, taskId2))
        assertEquals(2, result[taskId1])
        assertEquals(1, result[taskId2])
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

    private fun createSprintEntity(id: UUID = UUID.randomUUID()) = SprintEntity(
        id = id,
        project = createProjectEntity(),
        name = "Test Sprint",
        goal = "Test Goal",
        teamCapacity = 10,
        startDate = java.time.LocalDate.of(2023, 1, 1),
        endDate = java.time.LocalDate.of(2023, 1, 14),
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
        createdAt: Instant = Instant.parse("2023-01-01T09:00:00Z")
    ) = TaskEntity(
        id = id,
        story = createStoryEntity(),
        sprintColumn = createSprintColumnEntity(),
        title = "Test Task",
        description = "Test Description",
        createdBy = createUserEntity(),
        createdAt = createdAt
    )

    private fun createTaskEvent(
        taskId: UUID,
        eventType: TaskEventType,
        createdAt: String
    ) = TaskEventEntity(
        id = UUID.randomUUID(),
        taskId = taskId,
        eventType = eventType,
        oldValue = null,
        newValue = null,
        triggeredBy = createUserEntity(),
        createdAt = Instant.parse(createdAt)
    )

    private fun createSprintEvent(
        sprintId: UUID,
        eventType: SprintEventType,
        createdAt: String
    ) = SprintEventEntity(
        id = UUID.randomUUID(),
        sprintId = sprintId,
        eventType = eventType,
        oldValue = null,
        newValue = null,
        triggeredBy = createUserEntity(),
        createdAt = Instant.parse(createdAt)
    )

    private fun createSubtaskEntity(task: TaskEntity) = SubtaskEntity(
        id = UUID.randomUUID(),
        task = task,
        title = "Test Subtask",
        description = "Test Description",
        isDone = false,
        createdBy = createUserEntity(),
        createdAt = Instant.parse("2023-01-01T09:00:00Z")
    )

    private fun createSprintTimeline(
        started: Instant?,
        completed: Instant?
    ): Any {
        // Since SprintTimeline is private, we need to use reflection
        val timelineClass = calculator.javaClass.declaredClasses
            .first { it.simpleName == "SprintTimeline" }
        val constructor = timelineClass.getDeclaredConstructor(Instant::class.java, Instant::class.java)
        constructor.isAccessible = true
        return constructor.newInstance(started, completed)
    }

    // Reflection helper methods to test private methods
    private fun calculateCycleTimeReflection(events: List<TaskEventEntity>): Double {
        val method = calculator.javaClass.getDeclaredMethod("calculateCycleTime", List::class.java)
        method.isAccessible = true
        return method.invoke(calculator, events) as Double
    }

    private fun wasTaskReopenedReflection(events: List<TaskEventEntity>): Boolean {
        val method = calculator.javaClass.getDeclaredMethod("wasTaskReopened", List::class.java)
        method.isAccessible = true
        return method.invoke(calculator, events) as Boolean
    }

    private fun wasTaskAddedDuringSprintReflection(task: TaskEntity, timeline: Any): Boolean {
        val method = calculator.javaClass.getDeclaredMethod(
            "wasTaskAddedDuringSprint",
            TaskEntity::class.java,
            timeline.javaClass
        )
        method.isAccessible = true
        return method.invoke(calculator, task, timeline) as Boolean
    }

    private fun wasTaskRemovedDuringSprintReflection(events: List<TaskEventEntity>, timeline: Any): Boolean {
        val method =
            calculator.javaClass.getDeclaredMethod("wasTaskRemovedDuringSprint", List::class.java, timeline.javaClass)
        method.isAccessible = true
        return method.invoke(calculator, events, timeline) as Boolean
    }

    private fun wasTaskReassignedReflection(events: List<TaskEventEntity>): Boolean {
        val method = calculator.javaClass.getDeclaredMethod("wasTaskReassigned", List::class.java)
        method.isAccessible = true
        return method.invoke(calculator, events) as Boolean
    }

    private fun calculateCommentCountReflection(events: List<TaskEventEntity>): Int {
        val method = calculator.javaClass.getDeclaredMethod("calculateCommentCount", List::class.java)
        method.isAccessible = true
        return method.invoke(calculator, events) as Int
    }

    private fun isTaskCompletedReflection(events: List<TaskEventEntity>): Boolean {
        val method = calculator.javaClass.getDeclaredMethod("isTaskCompleted", List::class.java)
        method.isAccessible = true
        return method.invoke(calculator, events) as Boolean
    }

    private fun calculateTimeToFirstCommentReflection(events: List<TaskEventEntity>): Double? {
        val method = calculator.javaClass.getDeclaredMethod("calculateTimeToFirstComment", List::class.java)
        method.isAccessible = true
        return method.invoke(calculator, events) as Double?
    }

    private fun getLastActivityTimeReflection(events: List<TaskEventEntity>): Instant? {
        val method = calculator.javaClass.getDeclaredMethod("getLastActivityTime", List::class.java)
        method.isAccessible = true
        return method.invoke(calculator, events) as Instant?
    }

    private fun countAssignmentChangesReflection(events: List<TaskEventEntity>): Int {
        val method = calculator.javaClass.getDeclaredMethod("countAssignmentChanges", List::class.java)
        method.isAccessible = true
        return method.invoke(calculator, events) as Int
    }

    private fun countStatusTransitionsReflection(events: List<TaskEventEntity>): Int {
        val method = calculator.javaClass.getDeclaredMethod("countStatusTransitions", List::class.java)
        method.isAccessible = true
        return method.invoke(calculator, events) as Int
    }

    private fun countSubtasksReflection(taskIds: List<UUID>): Map<UUID, Int> {
        val method = calculator.javaClass.getDeclaredMethod("countSubtasks", List::class.java)
        method.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        return method.invoke(calculator, taskIds) as Map<UUID, Int>
    }
}