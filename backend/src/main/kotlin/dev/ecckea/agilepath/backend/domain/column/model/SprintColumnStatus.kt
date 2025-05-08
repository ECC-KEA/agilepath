package dev.ecckea.agilepath.backend.domain.column.model

enum class SprintColumnStatus {
    TODO,
    IN_PROGRESS,
    ARCHIVED,
    DONE;
    companion object {
        fun fromString(value: String): SprintColumnStatus {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown status: $value")
        }
    }
}