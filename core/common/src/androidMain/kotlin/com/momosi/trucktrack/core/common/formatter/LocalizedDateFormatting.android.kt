package com.momosi.trucktrack.core.common.formatter

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.Instant

internal actual fun createPlatformDateFormatter(): PlatformDateFormatter = AndroidPlatformDateFormatter()

private class AndroidPlatformDateFormatter : PlatformDateFormatter {

    override fun formatDateTime(instant: Instant): String = localizedFormatter("MMM d, j:mm").format(instant.toJavaDate())

    override fun formatShortDate(instant: Instant): String = localizedFormatter("MMM d").format(instant.toJavaDate())
}

private fun localizedFormatter(skeleton: String): SimpleDateFormat {
    val locale = Locale.getDefault()
    val pattern = DateFormat.getBestDateTimePattern(locale, skeleton)
    return SimpleDateFormat(pattern, locale)
}

private fun Instant.toJavaDate(): Date = Date(toEpochMilliseconds())
