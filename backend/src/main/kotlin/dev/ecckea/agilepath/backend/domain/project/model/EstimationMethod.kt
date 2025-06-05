package dev.ecckea.agilepath.backend.domain.project.model

enum class EstimationMethod {
    STORY_POINTS,
    TSHIRT_SIZES;

    companion object {
        fun fromString(value: String): EstimationMethod {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown estimation method: $value")
        }
    }
}