package com.momosi.trucktrack.feature.signin.impl

sealed interface SignInAction {
    data object SignInClick : SignInAction
}
