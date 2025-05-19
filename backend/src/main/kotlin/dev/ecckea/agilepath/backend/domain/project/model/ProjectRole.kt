package dev.ecckea.agilepath.backend.domain.project.model

enum class ProjectRole {
    OWNER, ADMIN, CONTRIBUTOR;

    companion object {
        fun fromString(value: String): ProjectRole {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown project role: $value")
        }
    }
}

