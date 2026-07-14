package com.momosi.trucktrack.user.model

sealed class TokenResponse {
    data class Token(val token: String) : TokenResponse()
    data class TokenError(val exception: Throwable) : TokenResponse()
    data object GuestWithoutToken : TokenResponse()
}
