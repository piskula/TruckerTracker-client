package com.momosi.trucktrack.app

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.network.ktor3.KtorNetworkFetcherFactory
import io.ktor.client.HttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

@OptIn(ExperimentalCoilApi::class)
internal class AppImageLoaderFactory :
    SingletonImageLoader.Factory,
    KoinComponent {
    private val imageHttpClient: HttpClient by inject(named("image"))

    override fun newImageLoader(context: PlatformContext): ImageLoader = ImageLoader.Builder(context)
        .components {
            add(KtorNetworkFetcherFactory(httpClient = imageHttpClient))
        }
        .build()
}
