package dev.ecckea.agilepath.backend.shared.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

private val defaultZone: ZoneId = ZoneId.of("Europe/Copenhagen")

fun toZonedDateTime(instant: Instant, zone: ZoneId = defaultZone): ZonedDateTime =
    instant.atZone(zone)

fun toLocalDateTime(instant: Instant, zone: ZoneId = defaultZone): LocalDateTime =
    LocalDateTime.ofInstant(instant, zone)

fun now(): Instant = Instant.now()

fun nowInZone(zone: ZoneId = defaultZone): ZonedDateTime =
    ZonedDateTime.now(zone)

//TODO Create unit tests for these conversions