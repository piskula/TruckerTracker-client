package sk.momosilabs.truckTrack.util

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

fun OffsetDateTime.toUtcLocalDateTime(): LocalDateTime =
    withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime()

fun LocalDateTime.toUtcOffsetDateTime(): OffsetDateTime =
    atOffset(ZoneOffset.UTC)
