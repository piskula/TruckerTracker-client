package com.momosi.trucktrack.core.common.formatter

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

private val MONTH_ABBREVS =
    listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

class DateFormatter {

    fun formatDateTime(instant: Instant): String {
        val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val month = MONTH_ABBREVS[local.monthNumber - 1]
        val hour = local.hour.toString().padStart(2, '0')
        val minute = local.minute.toString().padStart(2, '0')
        return "$month ${local.dayOfMonth}, $hour:$minute"
    }

    fun formatShortDate(instant: Instant): String {
        val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val month = MONTH_ABBREVS[local.monthNumber - 1]
        return "$month ${local.dayOfMonth}"
    }

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

sealed interface TimeAgo {
    data object JustNow : TimeAgo
    data class Minutes(val count: Long) : TimeAgo
    data class Hours(val count: Long) : TimeAgo
    data object Yesterday : TimeAgo
    data class Days(val count: Long) : TimeAgo
    data class OlderThanWeek(val formatted: String) : TimeAgo
}

