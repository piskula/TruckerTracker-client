package com.momosi.trucktrack.core.common.formatter

import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class DateFormatter {

    private val platformDateFormatter = createPlatformDateFormatter()

    fun formatDateTime(instant: Instant): String = platformDateFormatter.formatDateTime(instant)

    fun formatShortDate(instant: Instant): String = platformDateFormatter.formatShortDate(instant)

    fun timeAgoComponents(instant: Instant): TimeAgo {
        val now = Clock.System.now()
        val diff = now - instant
        return when {
            diff < 1.minutes -> TimeAgo.JustNow
            diff < 1.hours -> TimeAgo.Minutes(diff.inWholeMinutes)
            diff < 1.days -> TimeAgo.Hours(diff.inWholeHours)
            diff < 2.days -> TimeAgo.Yesterday
            diff < 7.days -> TimeAgo.Days(diff.inWholeDays)
            else -> TimeAgo.OlderThanWeek(formatShortDate(instant))
        }
    }
}

internal interface PlatformDateFormatter {
    fun formatDateTime(instant: Instant): String

    fun formatShortDate(instant: Instant): String
}

internal expect fun createPlatformDateFormatter(): PlatformDateFormatter

sealed interface TimeAgo {
    data object JustNow : TimeAgo
    data class Minutes(val count: Long) : TimeAgo
    data class Hours(val count: Long) : TimeAgo
    data object Yesterday : TimeAgo
    data class Days(val count: Long) : TimeAgo
    data class OlderThanWeek(val formatted: String) : TimeAgo
}
