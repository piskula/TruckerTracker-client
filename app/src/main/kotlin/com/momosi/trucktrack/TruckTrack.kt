package com.momosi.trucktrack

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.momosi.trucktrack.core.common.lifecycle.CurrentActivityHelper
import com.momosi.trucktrack.core.common.logger.Logger
import dagger.hilt.android.HiltAndroidApp
import io.ktor.client.HttpClient
import javax.inject.Inject

@HiltAndroidApp
class TruckTrack :
    Application(),
    SingletonImageLoader.Factory {

    @Inject
    lateinit var currentActivityHelper: CurrentActivityHelper

    @Inject
    lateinit var httpClient: HttpClient

    override fun onCreate() {
        super.onCreate()
        Logger.init(logToConsole = BuildConfig.DEBUG)
    }

    @OptIn(ExperimentalCoilApi::class)
    override fun newImageLoader(context: android.content.Context): ImageLoader = ImageLoader.Builder(context)
        .components {
            add(KtorNetworkFetcherFactory(httpClient = httpClient))
        }
        .build()
}
