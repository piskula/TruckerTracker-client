package com.momosi.trucktrack.user.internal

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.EndSessionResponse
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class OpenIdManager @Inject constructor(@ApplicationContext private val appContext: Context) {

    class UserCancelledException : RuntimeException()

    private var authServiceConfig: AuthorizationServiceConfiguration? = null
    private var pendingAuthorizationRequest: CancellableContinuation<Result<AuthorizationResponse>>? = null
    private var pendingEndSessionRequest: CancellableContinuation<Result<Unit>>? = null

    internal suspend fun authConfiguration(authRealmUrl: String) = authServiceConfig?.let {
        Result.success(it)
    } ?: suspendCancellableCoroutine {
        AuthorizationServiceConfiguration.fetchFromIssuer(authRealmUrl.toUri()) { serviceConfiguration, error ->
            authServiceConfig = serviceConfiguration
            val result = serviceConfiguration?.let { Result.success(serviceConfiguration) } ?: Result.failure(error ?: IllegalStateException("Auth configuration not available"))
            it.resume(result)
        }
    }

    internal suspend fun performActionWithFreshTokens(authState: AuthState) = suspendCancellableCoroutine {
        AuthorizationService(appContext).use { authService ->
            authState.performActionWithFreshTokens(authService) { accessToken, _, authorizationException ->
                val result = accessToken?.let { Result.success(authState) } ?: Result.failure(authorizationException ?: IllegalStateException("PerformActionWithFreshTokens failed"))
                it.resume(result)
            }
        }
    }

    internal suspend fun exchangeAuthResponseForToken(authResponse: AuthorizationResponse) = suspendCancellableCoroutine {
        AuthorizationService(appContext).use { authService ->
            authService.performTokenRequest(authResponse.createTokenExchangeRequest()) { tokenResponse, tokenException ->
                val result = tokenResponse?.let { Result.success(tokenResponse) } ?: Result.failure(tokenException ?: IllegalStateException("Exchanged failed"))
                it.resume(result)
            }
        }
    }

    internal suspend fun performEndSessionRequest(
        configuration: AuthorizationServiceConfiguration,
        idToken: String,
        postLogoutRedirectUri: String,
    ): Result<Unit> {
        val endSessionRequest = EndSessionRequest.Builder(configuration)
            .setIdTokenHint(idToken)
            .setPostLogoutRedirectUri(postLogoutRedirectUri.toUri())
            .build()

        AuthorizationService(appContext).use { authService ->
            authService.performEndSessionRequest(endSessionRequest, authActivityPendingIntent())
        }

        return suspendCancellableCoroutine { pendingEndSessionRequest = it }
    }

    internal suspend fun performAuthorizationRequest(activity: Activity, request: AuthorizationRequest): Result<AuthorizationResponse> {
        AuthorizationService(activity).use {
            it.performAuthorizationRequest(request, authActivityPendingIntent(), authActivityPendingIntent())
        }
        return suspendCancellableCoroutine {
            pendingAuthorizationRequest = it
        }
    }

    private fun AuthorizationService.use(block: (AuthorizationService) -> Unit) {
        block(this)
        dispose()
    }

    private fun authActivityPendingIntent() = PendingIntent.getActivity(
        appContext,
        0,
        Intent(appContext, AuthActivity::class.java),
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE else PendingIntent.FLAG_UPDATE_CURRENT,
    )

    internal fun onAuthIntentReceived(intent: Intent) {
        when {
            intent.hasExtra(EndSessionResponse.EXTRA_RESPONSE) -> {
                pendingEndSessionRequest?.resume(Result.success(Unit))
                pendingEndSessionRequest = null
            }

            intent.hasExtra(AuthorizationResponse.EXTRA_RESPONSE) -> {
                AuthorizationResponse.fromIntent(intent)?.let {
                    pendingAuthorizationRequest?.resume(Result.success(it))
                } ?: run {
                    val exception = AuthorizationException.fromIntent(intent) ?: IllegalStateException("Auth error")
                    pendingAuthorizationRequest?.resume(Result.failure(exception))
                }
                pendingAuthorizationRequest = null
            }

            // Cancelled
            else -> {
                pendingAuthorizationRequest?.let {
                    it.resume(Result.failure(UserCancelledException()))
                    pendingAuthorizationRequest = null
                }
                pendingEndSessionRequest?.let {
                    it.resume(Result.failure(UserCancelledException()))
                    pendingEndSessionRequest = null
                }
            }
        }
    }
}
