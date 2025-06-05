package dev.ecckea.agilepath.backend.domain.sprint.model

enum class SprintEventType {
    CREATED,
    STARTED,
    COMPLETED,
    GOAL_CHANGED,
    SPRINT_EXTENDED,
    SPRINT_SHORTENED,
    TASK_ADDED,
    TASK_REMOVED,
    REVIEW_COMPLETED,
    RETROSPECTIVE_COMPLETED,
    VELOCITY_CALCULATED
}