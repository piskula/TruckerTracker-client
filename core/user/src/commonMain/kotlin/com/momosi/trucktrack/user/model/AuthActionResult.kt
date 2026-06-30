package com.momosi.trucktrack.user.model

sealed class AuthActionResult {
    data class Success(val authenticationState: AuthenticationState) : AuthActionResult()
    sealed class Failed : AuthActionResult() {
        data object UserCancelled : Failed()
        data object NoInternet : Failed()
        data object NoActivity : Failed()
        data class Error(val throwable: Throwable) : Failed()
    }
}
