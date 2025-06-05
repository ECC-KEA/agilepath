package dev.ecckea.agilepath.backend.domain.analytics.application

import dev.ecckea.agilepath.backend.domain.analytics.model.*
import dev.ecckea.agilepath.backend.domain.analytics.service.AnalyticsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class AnalyticsApplication(
    private val analyticsService: AnalyticsService
) {

    fun getSprintAnalysis(sprintId: UUID): SprintAnalysis {
        return analyticsService.getSprintAnalysis(sprintId)
    }

    fun getSprintMetrics(sprintId: UUID): SprintMetrics {
        return analyticsService.getSprintMetrics(sprintId)
    }

    fun getSprintMetadata(sprintId: UUID): SprintMetadata {
        return analyticsService.getSprintMetadata(sprintId)
    }

    fun getTaskMetrics(sprintId: UUID): List<TaskMetrics> {
        return analyticsService.getTaskMetrics(sprintId)
    }

    fun getTaskMetrics(sprintId: UUID, taskId: UUID): TaskMetrics {
        return analyticsService.getTaskMetrics(sprintId, taskId)
    }

    fun getBurndownData(sprintId: UUID): BurndownData {
        return analyticsService.getBurndownData(sprintId)
    }

    fun compareSprintToPrevious(sprintId: UUID): SprintComparison {
        return analyticsService.compareSprintToPrevious(sprintId)
    }

    fun getSprintTrends(sprintId: UUID, sprintCount: Int): SprintTrends {
        return analyticsService.getSprintTrends(sprintId, sprintCount)
    }
}