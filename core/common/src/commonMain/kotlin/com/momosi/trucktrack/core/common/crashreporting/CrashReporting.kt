package com.momosi.trucktrack.core.common.crashreporting

import com.momosi.trucktrack.core.common.logger.Logger
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics

object CrashReporting {
    fun init() {
        initializeFirebase()
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(true)
        Logger.addWriter(CrashlyticsLogWriter())
    }
}

internal expect fun initializeFirebase()
