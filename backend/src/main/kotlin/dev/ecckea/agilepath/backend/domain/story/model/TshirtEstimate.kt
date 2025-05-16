package dev.ecckea.agilepath.backend.domain.story.model

enum class TshirtEstimate {
    XSMALL,
    SMALL,
    MEDIUM,
    LARGE,
    XLARGE;

    companion object {
        fun fromString(value: String): TshirtEstimate {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown tshirt estimate: $value")
        }
    }
}