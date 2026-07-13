package com.momosi.trucktrack

import android.app.Application
import com.momosi.trucktrack.app.initApp
import com.momosi.trucktrack.app.initKoin
import org.koin.android.ext.koin.androidContext

class TruckTrack : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin { androidContext(this@TruckTrack) }
        initApp(isDebug = BuildConfig.DEBUG)
    }
}
