package com.momosi.trucktrack.feature.profile.impl.di

import com.momosi.trucktrack.feature.profile.impl.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    viewModel { ProfileViewModel(get(), get(), get(), get(), get()) }
}
