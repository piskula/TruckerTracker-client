package com.momosi.trucktrack.core.common.di

import com.momosi.trucktrack.core.common.coroutines.DispatcherProvider
import com.momosi.trucktrack.core.common.formatter.DateFormatter
import org.koin.core.module.Module
import org.koin.dsl.module

val commonModule = module {
    single { DispatcherProvider() }
    single { DateFormatter() }
}

expect fun platformCommonModule(): Module
