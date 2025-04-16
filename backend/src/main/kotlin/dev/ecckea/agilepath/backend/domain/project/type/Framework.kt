package dev.ecckea.agilepath.backend.domain.project.type

enum class Framework {
    SCRUM,
    XP,
    NONE;

    companion object {
        fun fromString(value: String): Framework {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown framework: $value")
        }
    }
}