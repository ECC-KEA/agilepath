package dev.ecckea.agilepath.backend.domain.story.dto

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TaskRequestValidationTest {
    private lateinit var validator: Validator

    @BeforeEach
    fun setUp() {
        val factory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }

    private fun createValidRequest(): TaskRequest {
        return TaskRequest(
            storyId = UUID.randomUUID(),
            sprintColumnId = UUID.randomUUID(),
            title = "Valid Task Title",
            description = "Valid description",
            estimateTshirt = "MEDIUM",
            estimatePoints = null,
            assigneeIds = listOf()
        )
    }

    private fun assertHasViolation(request: TaskRequest, message: String) {
        val violations = validator.validate(request)
        assertFalse(violations.isEmpty(), "Expected validation violations but found none")
        assertTrue(
            violations.any { it.message.contains(message) },
            "Expected violation with message containing: $message, but found: ${violations.map { it.message }}"
        )
    }

    private fun assertNoViolations(request: TaskRequest) {
        val violations = validator.validate(request)
        assertTrue(violations.isEmpty(), "Expected no violations but found: ${violations.map { it.message }}")
    }

    private fun assertNoEstimateViolations(request: TaskRequest) {
        val violations = validator.validate(request)
        val estimateViolations = violations.filter {
            it.message.contains("Exactly one estimate type") ||
                    it.message.contains("must be a valid enum value")
        }
        assertTrue(
            estimateViolations.isEmpty(),
            "Expected no estimate violations but found: ${estimateViolations.map { it.message }}"
        )
    }

    @Test
    fun `should reject blank title`() {
        val request = createValidRequest().copy(title = "   ")
        assertHasViolation(request, "Title name must not be blank")
    }

    @Test
    fun `should reject too short title`() {
        val request = createValidRequest().copy(title = "ab")
        assertHasViolation(request, "Title name must be between 3 and 100 characters")
    }

    @Test
    fun `should reject too long title`() {
        val request = createValidRequest().copy(title = "a".repeat(101))
        assertHasViolation(request, "Title name must be between 3 and 100 characters")
    }

    @Test
    fun `should reject HTML in description`() {
        val request = createValidRequest().copy(description = "<script>alert('xss')</script>")
        assertHasViolation(request, "Description must not contain HTML")
    }

    @Test
    fun `should reject too long description`() {
        val request = createValidRequest().copy(description = "a".repeat(2001))
        assertHasViolation(request, "Description must be at most 2000 characters")
    }

    @Test
    fun `should reject invalid t-shirt estimate`() {
        val request = createValidRequest().copy(
            estimateTshirt = "INVALID_SIZE",
            estimatePoints = null
        )
        assertHasViolation(request, "must be XSMALL, SMALL, MEDIUM, LARGE or XLARGE")
    }

    @Test
    fun `should reject invalid point estimate`() {
        val request = createValidRequest().copy(
            estimateTshirt = null,
            estimatePoints = "POINT_100"
        )
        assertHasViolation(request, "must be POINT_1, POINT_2, POINT_3, POINT_5, POINT_8, POINT_13 or POINT_21")
    }

    @Test
    fun `should reject when both estimates are null`() {
        val request = createValidRequest().copy(
            estimateTshirt = null,
            estimatePoints = null
        )
        assertHasViolation(request, "Either T-shirt estimate or point estimate must be provided")
    }

    @Test
    fun `should reject when both estimates are provided`() {
        val request = createValidRequest().copy(
            estimateTshirt = "MEDIUM",
            estimatePoints = "POINT_5"
        )
        assertHasViolation(request, "Either T-shirt estimate or point estimate must be provided")
    }

    @Test
    fun `should validate with only t-shirt estimate`() {
        val request = createValidRequest().copy(
            estimateTshirt = "MEDIUM",
            estimatePoints = null
        )
        assertNoEstimateViolations(request)
    }

    @Test
    fun `should validate with only point estimate`() {
        val request = createValidRequest().copy(
            estimateTshirt = null,
            estimatePoints = "POINT_5"
        )
        assertNoEstimateViolations(request)
    }

    @Test
    fun `should validate with t-shirt estimate in any case`() {
        val request = createValidRequest().copy(
            estimateTshirt = "medium", // lowercase
            estimatePoints = null
        )
        assertNoEstimateViolations(request)

        val anotherRequest = createValidRequest().copy(
            estimateTshirt = "MEDIUM", // uppercase
            estimatePoints = null
        )
        assertNoEstimateViolations(anotherRequest)
    }

    @Test
    fun `should validate with point estimate in any case`() {
        val request = createValidRequest().copy(
            estimateTshirt = null,
            estimatePoints = "point_5" // lowercase
        )
        assertNoEstimateViolations(request)

        val anotherRequest = createValidRequest().copy(
            estimateTshirt = null,
            estimatePoints = "POINT_5" // uppercase
        )
        assertNoEstimateViolations(anotherRequest)
    }
}