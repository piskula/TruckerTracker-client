package com.momosi.trucktrack.core.common.crashreporting

import android.content.Context
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import org.koin.core.context.GlobalContext

internal actual fun initializeFirebase() {
    Firebase.initialize(GlobalContext.get().get<Context>())
}
