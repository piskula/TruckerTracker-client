package com.momosi.trucktrack.core.common.crashreporting

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize

internal actual fun initializeFirebase() {
    Firebase.initialize()
}
