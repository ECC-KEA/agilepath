package dev.ecckea.agilepath.backend.domain.story.model

enum class PointEstimate(val points: Int) {
    POINT_1(1),
    POINT_2(2),
    POINT_3(3),
    POINT_5(5),
    POINT_8(8),
    POINT_13(13),
    POINT_21(21);

    companion object {
        fun fromString(value: String): PointEstimate {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown point estimate: $value")
        }

        fun fromPoints(points: Int): PointEstimate {
            return entries.firstOrNull { it.points == points }
                ?: throw IllegalArgumentException("Unknown point estimate: $points")
        }
    }
}