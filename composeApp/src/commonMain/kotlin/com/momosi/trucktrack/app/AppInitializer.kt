package com.momosi.trucktrack.app

import com.momosi.trucktrack.app.di.allModules
import com.momosi.trucktrack.core.common.logger.Logger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initKoin(platformConfig: KoinApplication.() -> Unit = {}) {
    startKoin {
        platformConfig()
        modules(allModules)
    }
}

fun initApp(isDebug: Boolean) {
    Logger.init(logToConsole = isDebug)
}
