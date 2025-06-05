package dev.ecckea.agilepath.backend.domain.story.model

enum class TaskEventType {
    CREATED,
    STARTED,
    COMPLETED,
    REOPENED,
    ASSIGNED,
    UNASSIGNED,
    REASSIGNED,
    ESTIMATED,
    TITLE_CHANGED,
    DESCRIPTION_CHANGED,
    SPRINT_COLUMN_CHANGED,
    ESTIMATE_CHANGED,
    COMMENT_ADDED,
    DELETED
}