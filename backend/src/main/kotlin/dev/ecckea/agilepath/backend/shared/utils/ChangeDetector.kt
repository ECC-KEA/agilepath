package dev.ecckea.agilepath.backend.shared.utils

import kotlin.reflect.full.memberProperties

/**
 * Utility object for detecting changes between two instances of the same class.
 *
 * This object uses Kotlin reflection to compare the properties of two objects
 * and returns a result containing the properties that have different values.
 */
object ChangeDetector {

    /**
     * Detects changes between two instances of the same class.
     *
     * @param T The type of the objects being compared. Must be a non-nullable type.
     * @param old The original object to compare.
     * @param new The updated object to compare.
     * @param excludedProperties A set of property names to exclude from the comparison.
     * @return A `ChangeResult` containing the list of detected changes.
     * @throws IllegalArgumentException if `old` and `new` are not of the same type.
     */
    fun <T : Any> detectChanges(
        old: T,
        new: T,
        excludedProperties: Set<String> = emptySet(),
    ): ChangeResult {

        require(old::class == new::class) {
            "Old and new objects must be of the same type. Got ${old::class.simpleName} and ${new::class.simpleName}"
        }

        val changes = mutableListOf<PropertyChange>()
        val kClass = old::class

        // Iterate through all member properties of the class
        kClass.memberProperties.forEach { prop ->
            if (prop.name in excludedProperties) return@forEach

            val oldValue = prop.call(old)
            val newValue = prop.call(new)

            // Compare values and add to the list of changes if they differ
            if (!areEqual(oldValue, newValue)) {
                changes.add(
                    PropertyChange(
                        propertyName = prop.name,
                        oldValue = oldValue,
                        newValue = newValue
                    )
                )
            }
        }

        return ChangeResult(changes)
    }

    /**
     * Compares two values for equality, with special handling for collections and arrays.
     *
     * @param oldValue The original value to compare.
     * @param newValue The updated value to compare.
     * @return `true` if the values are considered equal, `false` otherwise.
     */
    private fun areEqual(oldValue: Any?, newValue: Any?): Boolean {
        return when {
            oldValue === newValue -> true
            oldValue == null || newValue == null -> false
            oldValue is Collection<*> && newValue is Collection<*> -> {
                oldValue.size == newValue.size && oldValue.containsAll(newValue)
            }

            oldValue is Array<*> && newValue is Array<*> -> {
                oldValue.contentDeepEquals(newValue)
            }

            else -> oldValue == newValue
        }
    }
}

/**
 * Represents a change detected in a property.
 *
 * @param propertyName The name of the property that changed.
 * @param oldValue The original value of the property.
 * @param newValue The updated value of the property.
 */
data class PropertyChange(
    val propertyName: String,
    val oldValue: Any?,
    val newValue: Any?
)

/**
 * Represents the result of a change detection operation.
 *
 * @param changes A list of detected property changes.
 */
data class ChangeResult(
    val changes: List<PropertyChange>
) {
    /**
     * Checks if any changes were detected.
     *
     * @return `true` if there are changes, `false` otherwise.
     */
    fun hasChanges(): Boolean = changes.isNotEmpty()

    /**
     * Retrieves the change for a specific property.
     *
     * @param propertyName The name of the property to retrieve the change for.
     * @return The `PropertyChange` for the specified property.
     * @throws NoSuchElementException if no change is found for the property.
     */
    fun getChange(propertyName: String): PropertyChange? =
        changes.find { it.propertyName == propertyName }
            ?: throw NoSuchElementException("No change found for property '$propertyName'")

    /**
     * Checks if a specific property has changes.
     *
     * @param propertyName The name of the property to check.
     * @return `true` if the property has changes, `false` otherwise.
     */
    fun contains(propertyName: String): Boolean =
        changes.any { it.propertyName == propertyName }
}