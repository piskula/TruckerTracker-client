package com.momosi.trucktrack.core.common.crashreporting

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics

internal class CrashlyticsLogWriter : LogWriter() {
    override fun log(
        severity: Severity,
        message: String,
        tag: String,
        throwable: Throwable?,
    ) {
        Firebase.crashlytics.log("[$tag] $message")
        throwable?.let { Firebase.crashlytics.recordException(it) }
    }
}
