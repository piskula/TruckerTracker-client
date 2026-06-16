package com.momosi.trucktrack.user.internal.networking

import com.momosi.trucktrack.core.common.logger.Logger
import com.momosi.trucktrack.user.AuthManager
import com.momosi.trucktrack.user.model.TokenResponse
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

private const val TAG = "UserAuthenticator"

class UserAuthenticator @Inject constructor(
    private val authManager: AuthManager,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        return when (val tokenResponse = runBlocking { authManager.token() }) {
            is TokenResponse.GuestWithoutToken -> {
                Logger.d(TAG, "Authenticator challenge for guest - token is not available")
                null
            }

            is TokenResponse.TokenError -> {
                Logger.d(TAG, "Authenticator challenge - can not obtain token")
                null
            }

            is TokenResponse.Token -> {
                Logger.d(TAG, "Authenticator challenge successful")
                response.request.withAuthHeader(token = tokenResponse.token)
            }
        }
    }
}
