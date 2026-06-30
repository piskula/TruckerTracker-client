package com.momosi.trucktrack

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.momosi.trucktrack.core.common.di.commonModule
import com.momosi.trucktrack.core.common.lifecycle.CurrentActivityHelper
import com.momosi.trucktrack.core.common.logger.Logger
import com.momosi.trucktrack.core.issue.di.issueModule
import com.momosi.trucktrack.core.network.di.networkModule
import com.momosi.trucktrack.core.vehicle.di.vehicleModule
import com.momosi.trucktrack.dependencyinjection.applicationModule
import com.momosi.trucktrack.feature.issues.impl.di.issuesModule
import com.momosi.trucktrack.feature.profile.impl.di.profileModule
import com.momosi.trucktrack.feature.signin.impl.di.signInModule
import com.momosi.trucktrack.user.di.userModule
import io.ktor.client.HttpClient
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named

class TruckTrack :
    Application(),
    SingletonImageLoader.Factory {

    private val currentActivityHelper: CurrentActivityHelper by inject()

    private val httpClient: HttpClient by inject(qualifier = named("image"))

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TruckTrack)
            modules(
                commonModule,
                networkModule,
                userModule,
                vehicleModule,
                issueModule,
                applicationModule,
                signInModule,
                issuesModule,
                profileModule,
            )
        }
        currentActivityHelper
        Logger.init(logToConsole = BuildConfig.DEBUG)
    }

    @OptIn(ExperimentalCoilApi::class)
    override fun newImageLoader(context: Context): ImageLoader = ImageLoader.Builder(context)
        .components {
            add(KtorNetworkFetcherFactory(httpClient = httpClient))
        }
        .build()
}
