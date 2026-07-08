package com.momosi.trucktrack.core.common.di

import com.momosi.trucktrack.core.common.network.ConnectivityManager
import com.momosi.trucktrack.core.common.network.ConnectivityManagerImpl
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformCommonModule(): Module = module {
    single<ConnectivityManager> { ConnectivityManagerImpl() }
}
