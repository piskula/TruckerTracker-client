package com.momosi.trucktrack.core.common.di

import android.content.Context
import com.momosi.trucktrack.core.common.coroutines.DispatcherProvider
import com.momosi.trucktrack.core.common.formatter.DateFormatter
import com.momosi.trucktrack.core.common.io.PhotoReader
import com.momosi.trucktrack.core.common.io.PhotoReaderImpl
import com.momosi.trucktrack.core.common.lifecycle.CurrentActivityHelper
import com.momosi.trucktrack.core.common.network.ConnectivityManager
import com.momosi.trucktrack.core.common.network.ConnectivityManagerImpl
import org.koin.dsl.module

val commonModule = module {
    single { DispatcherProvider() }
    single { DateFormatter() }
    single { get<Context>().getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager }
    single<ConnectivityManager> { ConnectivityManagerImpl(get()) }
    single { CurrentActivityHelper(get()) }
    single<PhotoReader> { PhotoReaderImpl(get()) }
}
