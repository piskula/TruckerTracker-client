package com.momosi.trucktrack.feature.signin.impl.di

import com.momosi.trucktrack.feature.signin.impl.SignInViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val signInModule = module {
    viewModel { SignInViewModel(get()) }
}
