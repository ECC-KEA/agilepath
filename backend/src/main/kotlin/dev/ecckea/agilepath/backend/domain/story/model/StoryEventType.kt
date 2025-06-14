package dev.ecckea.agilepath.backend.domain.story.model

enum class StoryEventType {
    CREATED,
    STATUS_CHANGED,
    REOPENED,
    TITLE_CHANGED,
    DESCRIPTION_CHANGED,
    PRIORITY_CHANGED,
    TASK_ADDED,
    TASK_REMOVED,
    COMMENT_ADDED,
    ACCEPTANCE_CRITERIA_ADDED,
    ACCEPTANCE_CRITERIA_CHANGED,
    ACCEPTANCE_CRITERIA_REMOVED,
    COMPLETED,
    DELETED
}