package com.momosi.trucktrack.dependencyinjection

import com.momosi.trucktrack.ui.TruckTrackViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {
    single { CoroutineScope(SupervisorJob()) }
    viewModel { TruckTrackViewModel(get()) }
}
