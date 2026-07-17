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

    fun setUserId(userId: String?) {
        Firebase.crashlytics.setUserId(userId.orEmpty())
    }

    fun setCustomKey(key: String, value: String) {
        Firebase.crashlytics.setCustomKey(key, value)
    }
}

internal expect fun initializeFirebase()
