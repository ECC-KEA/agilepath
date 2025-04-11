package dev.ecckea.agilepath.backend.shared.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.ZoneId

class TimeConversionUtilsUnitTest {

 @Test
 fun convertsInstantToZonedDateTimeWithDefaultZone() {
  val instant = Instant.parse("2023-01-01T12:00:00Z")
  val result = toZonedDateTime(instant)
  assertEquals("Europe/Copenhagen", result.zone.id)
  assertEquals(2023, result.year)
  assertEquals(1, result.monthValue)
  assertEquals(1, result.dayOfMonth)
  assertEquals(13, result.hour) // Adjusted for Copenhagen time
  assertEquals(0, result.minute)
  assertEquals(0, result.second)
 }

 @Test
 fun convertsInstantToZonedDateTimeWithCustomZone() {
  val instant = Instant.parse("2023-01-01T12:00:00Z")
  val customZone = ZoneId.of("America/New_York")
  val result = toZonedDateTime(instant, customZone)
  assertEquals("America/New_York", result.zone.id)
  assertEquals(2023, result.year)
  assertEquals(1, result.monthValue)
  assertEquals(1, result.dayOfMonth)
  assertEquals(7, result.hour) // Adjusted for New York time
  assertEquals(0, result.minute)
  assertEquals(0, result.second)
 }

 @Test
 fun convertsInstantToLocalDateTimeWithDefaultZone() {
  val instant = Instant.parse("2023-01-01T12:00:00Z")
  val result = toLocalDateTime(instant)
  assertEquals(2023, result.year)
  assertEquals(1, result.monthValue)
  assertEquals(1, result.dayOfMonth)
  assertEquals(13, result.hour) // Adjusted for Copenhagen time
  assertEquals(0, result.minute)
  assertEquals(0, result.second)
 }

 @Test
 fun convertsInstantToLocalDateTimeWithCustomZone() {
  val instant = Instant.parse("2023-01-01T12:00:00Z")
  val customZone = ZoneId.of("America/New_York")
  val result = toLocalDateTime(instant, customZone)
  assertEquals(2023, result.year)
  assertEquals(1, result.monthValue)
  assertEquals(1, result.dayOfMonth)
  assertEquals(7, result.hour) // Adjusted for New York time
  assertEquals(0, result.minute)
  assertEquals(0, result.second)
 }

 @Test
 fun returnsCurrentInstant() {
  val before = Instant.now()
  val result = now()
  val after = Instant.now()
  assertTrue(result.isAfter(before) || result == before)
  assertTrue(result.isBefore(after) || result == after)
 }

 @Test
 fun returnsCurrentZonedDateTimeWithDefaultZone() {
  val result = nowInZone()
  assertEquals("Europe/Copenhagen", result.zone.id)
  assertTrue(result.toInstant().isBefore(Instant.now()) || result.toInstant() == Instant.now())
 }

 @Test
 fun returnsCurrentZonedDateTimeWithCustomZone() {
  val customZone = ZoneId.of("America/New_York")
  val result = nowInZone(customZone)
  assertEquals("America/New_York", result.zone.id)
  assertTrue(result.toInstant().isBefore(Instant.now()) || result.toInstant() == Instant.now())
 }
}