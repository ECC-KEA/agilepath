package dev.ecckea.agilepath.backend.shared.utils

import java.time.*

private val defaultZone: ZoneId = ZoneId.of("Europe/Copenhagen")

fun toZonedDateTime(instant: Instant, zone: ZoneId = defaultZone): ZonedDateTime =
    instant.atZone(zone)

fun toLocalDateTime(instant: Instant, zone: ZoneId = defaultZone): LocalDateTime =
    LocalDateTime.ofInstant(instant, zone)

fun toLocalDate(instant: Instant, zone: ZoneId = defaultZone): LocalDate =
    LocalDateTime.ofInstant(instant, zone).toLocalDate()

fun now(): Instant = Instant.now()

fun nowInZone(zone: ZoneId = defaultZone): ZonedDateTime =
    ZonedDateTime.now(zone)