package dev.ecckea.agilepath.backend.domain.analytics.controller

import dev.ecckea.agilepath.backend.domain.analytics.application.AnalyticsApplication
import dev.ecckea.agilepath.backend.domain.analytics.dto.*
import dev.ecckea.agilepath.backend.domain.analytics.model.mapper.toDTO
import dev.ecckea.agilepath.backend.shared.logging.Logged
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/analytics")
@Tag(name = "Analytics", description = "Sprint and task analytics API")
class AnalyticsController(
    private val analyticsApplication: AnalyticsApplication
) : Logged() {

    // SPRINT ANALYSIS ENDPOINTS

    @GetMapping("/sprints/{sprintId}/analysis")
    @Operation(
        summary = "Get comprehensive sprint analysis",
        description = "Returns complete sprint analysis including metrics, metadata, task metrics, and insights",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Sprint analysis retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "404", description = "Sprint not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getSprintAnalysis(
        @Parameter(description = "Sprint ID", required = true)
        @PathVariable sprintId: UUID
    ): SprintAnalysisResponse {
        return analyticsApplication.getSprintAnalysis(sprintId).toDTO()
    }

    @GetMapping("/sprints/{sprintId}/metrics")
    @Operation(
        summary = "Get sprint metrics",
        description = "Returns sprint performance metrics including velocity, cycle time, and collaboration scores",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Sprint metrics retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "404", description = "Sprint not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getSprintMetrics(
        @Parameter(description = "Sprint ID", required = true)
        @PathVariable sprintId: UUID
    ): SprintMetricsResponse {
        return analyticsApplication.getSprintMetrics(sprintId).toDTO()
    }

    @GetMapping("/sprints/{sprintId}/metadata")
    @Operation(
        summary = "Get sprint metadata",
        description = "Returns basic sprint information including capacity, task counts, and planned points",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Sprint metadata retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "404", description = "Sprint not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getSprintMetadata(
        @Parameter(description = "Sprint ID", required = true)
        @PathVariable sprintId: UUID
    ): SprintMetadataResponse {
        return analyticsApplication.getSprintMetadata(sprintId).toDTO()
    }

    // TASK ANALYSIS ENDPOINTS

    @GetMapping("/sprints/{sprintId}/tasks/metrics")
    @Operation(
        summary = "Get task metrics for all tasks in sprint",
        description = "Returns detailed metrics for every task in the sprint",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Task metrics retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "404", description = "Sprint not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getTaskMetrics(
        @Parameter(description = "Sprint ID", required = true)
        @PathVariable sprintId: UUID
    ): List<TaskMetricsResponse> {
        return analyticsApplication.getTaskMetrics(sprintId).map { it.toDTO() }
    }

    @GetMapping("/sprints/{sprintId}/tasks/{taskId}/metrics")
    @Operation(
        summary = "Get metrics for a specific task",
        description = "Returns detailed metrics for a single task",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Task metrics retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "404", description = "Sprint or task not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getTaskMetrics(
        @Parameter(description = "Sprint ID", required = true)
        @PathVariable sprintId: UUID,
        @Parameter(description = "Task ID", required = true)
        @PathVariable taskId: UUID
    ): TaskMetricsResponse {

        return analyticsApplication.getTaskMetrics(sprintId, taskId).toDTO()
    }

    // BURNDOWN CHART ENDPOINTS

    @GetMapping("/sprints/{sprintId}/burndown")
    @Operation(
        summary = "Get burndown chart data",
        description = "Returns burndown chart data with optimal path and actual progress",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Burndown data retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "404", description = "Sprint not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getBurndownData(
        @Parameter(description = "Sprint ID", required = true)
        @PathVariable sprintId: UUID
    ): BurndownResponse {
        return analyticsApplication.getBurndownData(sprintId).toDTO()
    }

    // COMPARATIVE ANALYSIS ENDPOINTS

    @GetMapping("/sprints/{sprintId}/comparison")
    @Operation(
        summary = "Compare sprint to previous sprint",
        description = "Compares current sprint metrics to the previous sprint in the same project",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Sprint comparison retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "404", description = "Sprint not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun compareSprintToPrevious(
        @Parameter(description = "Current sprint ID", required = true)
        @PathVariable sprintId: UUID
    ): SprintComparisonResponse {
        return analyticsApplication.compareSprintToPrevious(sprintId).toDTO()
    }

    @GetMapping("/projects/{projectId}/trends")
    @Operation(
        summary = "Get sprint trends for project",
        description = "Returns trend analysis for the recent sprints in a project",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Sprint trends retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized – Missing or invalid JWT"),
            ApiResponse(responseCode = "404", description = "Project not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getSprintTrends(
        @Parameter(description = "Project ID", required = true)
        @PathVariable projectId: UUID,
        @Parameter(description = "Number of recent sprints to analyze", required = false)
        @RequestParam(defaultValue = "3") sprintCount: Int
    ): SprintTrendsResponse {
        return analyticsApplication.getSprintTrends(projectId, sprintCount).toDTO()
    }
}