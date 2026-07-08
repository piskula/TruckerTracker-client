package com.momosi.trucktrack

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.momosi.trucktrack.app.initApp
import com.momosi.trucktrack.app.initKoin
import io.ktor.client.HttpClient
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named

class TruckTrack :
    Application(),
    SingletonImageLoader.Factory {

    private val httpClient: HttpClient by inject(qualifier = named("image"))

    override fun onCreate() {
        super.onCreate()
        initKoin { androidContext(this@TruckTrack) }
        initApp(isDebug = BuildConfig.DEBUG)
    }

    @OptIn(ExperimentalCoilApi::class)
    override fun newImageLoader(context: Context): ImageLoader = ImageLoader.Builder(context)
        .components {
            add(KtorNetworkFetcherFactory(httpClient = httpClient))
        }
        .build()
}
