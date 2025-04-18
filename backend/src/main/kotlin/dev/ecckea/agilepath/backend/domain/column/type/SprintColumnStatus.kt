package dev.ecckea.agilepath.backend.domain.column.type

enum class SprintColumnStatus {
    TODO,
    IN_PROGRESS,
    DONE;
    companion object {
        fun fromString(value: String): SprintColumnStatus {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown status: $value")
        }
    }
}