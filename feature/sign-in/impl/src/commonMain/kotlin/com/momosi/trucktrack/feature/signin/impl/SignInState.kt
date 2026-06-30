package com.momosi.trucktrack.feature.signin.impl

sealed interface SignInState {
    data object Idle : SignInState
    data object Loading : SignInState

    sealed interface Error : SignInState {
        data object NoInternet : Error
        data object NoActivity : Error
        data object Generic : Error
    }
}
