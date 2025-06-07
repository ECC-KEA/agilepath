package dev.ecckea.agilepath.backend.domain.story.model

enum class TshirtEstimate(val points: Int) {
    XSMALL(1),
    SMALL(2),
    MEDIUM(3),
    LARGE(5),
    XLARGE(8);

    companion object {
        fun fromString(value: String): TshirtEstimate {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown tshirt estimate: $value")
        }
    }
}