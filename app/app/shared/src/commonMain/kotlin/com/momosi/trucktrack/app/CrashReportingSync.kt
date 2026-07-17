package com.momosi.trucktrack.app

import com.momosi.trucktrack.core.common.crashreporting.CrashReporting
import com.momosi.trucktrack.core.common.network.ConnectivityManager
import com.momosi.trucktrack.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CrashReportingSync(userRepository: UserRepository, connectivityManager: ConnectivityManager, appCoroutineScope: CoroutineScope) {
    init {
        userRepository.user
            .onEach { user ->
                CrashReporting.setUserId(user?.id)
                CrashReporting.setCustomKey("userRole", user?.roles?.joinToString(",") { it.name }.orEmpty())
            }
            .launchIn(appCoroutineScope)

        connectivityManager.observeConnectionType()
            .onEach { CrashReporting.setCustomKey("connectionType", it.name) }
            .launchIn(appCoroutineScope)
    }
}
