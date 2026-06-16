package com.momosi.trucktrack.user.internal.networking

import com.momosi.trucktrack.core.common.logger.Logger
import com.momosi.trucktrack.core.common.network.ConnectivityManager
import com.momosi.trucktrack.user.AuthManager
import com.momosi.trucktrack.user.model.TokenResponse
import kotlinx.coroutines.runBlocking
import net.openid.appauth.AuthorizationException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.time.Duration.Companion.seconds

class UserAuthorizationInterceptor(
    private val authManager: AuthManager,
    private val connectivityManager: ConnectivityManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = runCatching {
            chain.authorizedRequest()
        }.getOrElse { e ->
            if (shouldRetry(e)) {
                Logger.d(TAG, "Retrying get token")
                Thread.sleep(RETRY_DELAY)
                chain.authorizedRequest()
            } else {
                throw e
            }
        }
        return chain.proceed(request)
    }

    private fun Interceptor.Chain.authorizedRequest(): Request {
        return when (val tokenResponse = runBlocking { authManager.token() }) {
            is TokenResponse.GuestWithoutToken -> request()
            is TokenResponse.Token -> request().withAuthHeader(token = tokenResponse.token)
            is TokenResponse.TokenError -> {
                throw IOException(tokenResponse.exception)
            }
        }
    }

    private fun shouldRetry(t: Throwable) = with(t.cause) {
        connectivityManager.isNetworkAvailable && this is AuthorizationException && code == AUTHORIZATION_NETWORK_ERROR
    }

    companion object {
        private const val TAG = "UserAuthorizationInterceptorImpl"
        private const val AUTHORIZATION_NETWORK_ERROR = 3
        private val RETRY_DELAY = 2.seconds.inWholeMilliseconds
    }
}
