package com.momosi.trucktrack

import android.app.Application
import com.momosi.trucktrack.core.common.lifecycle.CurrentActivityHelper
import com.momosi.trucktrack.core.common.logger.Logger
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TruckTrack : Application() {

    @Inject
    lateinit var  currentActivityHelper: CurrentActivityHelper

    override fun onCreate() {
        super.onCreate()
        Logger.init(logToConsole = BuildConfig.DEBUG)
    }
}
