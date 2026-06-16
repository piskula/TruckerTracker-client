package com.momosi.trucktrack.user

import androidx.core.net.toUri
import com.momosi.trucktrack.core.common.lifecycle.CurrentActivityHelper
import com.momosi.trucktrack.core.common.logger.Logger
import com.momosi.trucktrack.core.common.network.ConnectivityManager
import com.momosi.trucktrack.user.internal.OpenIdManager
import com.momosi.trucktrack.user.internal.TokenVerificationException
import com.momosi.trucktrack.user.internal.TokenVerifier
import com.momosi.trucktrack.user.internal.UserStorage
import com.momosi.trucktrack.user.model.AuthActionResult
import com.momosi.trucktrack.user.model.AuthenticationState
import com.momosi.trucktrack.user.model.TokenResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.ResponseTypeValues
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "UserAuthManager"
internal const val REALM_URL = "https://sso.momosi.org/realms/trucktrack/"
private const val CLIENT_ID = "trucktrack-app"
private const val REDIRECT_URL = "com.momosi.trucktrack://auth/callback"
private const val POST_LOGOUT_REDIRECT_URL = "com.momosi.trucktrack://auth/logout"

@Singleton
class AuthManagerImpl @Inject constructor(
    private val currentActivityHelper: CurrentActivityHelper,
    private val connectivityManager: ConnectivityManager,
    private val openIdManager: OpenIdManager,
    private val authStorage: UserStorage,
    private val tokenVerifier: TokenVerifier,
    appCoroutineScope: CoroutineScope,
) : AuthManager {

    override val authenticationState: StateFlow<AuthenticationState>
        field = MutableStateFlow(
            when {
                authStorage.authState.isAuthorized -> AuthenticationState.Authorized
                else -> AuthenticationState.Guest
            },
        )

    override val authenticationInProgress: StateFlow<Boolean>
        field = MutableStateFlow(false)

    private val refreshTokenMutex = Mutex()

    init {
        authenticationState
            .onEach {
                val authState = when (it) {
                    AuthenticationState.Guest -> "Guest"
                    AuthenticationState.Authorized -> "Authorized"
                }
                Logger.i(TAG, "Auth state: $authState")
            }
            .launchIn(appCoroutineScope)
    }

    override suspend fun signIn(): AuthActionResult {
        if (!connectivityManager.isNetworkAvailable) {
            return AuthActionResult.Failed.NoInternet
        }
        val activity = currentActivityHelper.activity ?: run {
            return AuthActionResult.Failed.NoActivity
        }

        authenticationInProgress.value = true

        val configuration = openIdManager.authConfiguration(REALM_URL).getOrElse {
            authenticationInProgress.value = false
            return AuthActionResult.Failed.Error(IllegalStateException("Can not resolve auth configuration"))
        }

        Logger.d(TAG, "Start authorization web flow")

        val authorizationRequest = AuthorizationRequest.Builder(
            configuration,
            CLIENT_ID,
            ResponseTypeValues.CODE,
            REDIRECT_URL.toUri(),
        )
            .setScopes("openid", "offline_access")
            .setPrompt("login")
            .build()

        return runCatching {
            openIdManager.performAuthorizationRequest(activity, authorizationRequest)
                .onSuccess { Logger.d(TAG, "Authorization finished with success response. Let's exchange token") }
                .flatMap { authResponse ->
                    openIdManager.exchangeAuthResponseForToken(authResponse)
                        .onSuccess { Logger.d(TAG, "Token exchanged successfully") }
                        .map { tokenResponse -> AuthState(authResponse, tokenResponse, null) }
                }
                .verified()
                .onSuccess { authState ->
                    Logger.d(TAG, "Authorization state successfully updated")
                    updateAuthState(authState)
                    authenticationInProgress.value = false
                    authenticationState.value = AuthenticationState.Authorized
                }
                .onFailure {
                    authenticationInProgress.value = false
                }
                .map { AuthActionResult.Success(AuthenticationState.Authorized) }
                .getOrElse { exception ->
                    return if (exception is OpenIdManager.UserCancelledException) {
                        Logger.d(TAG, "User closed auth flow")
                        AuthActionResult.Failed.UserCancelled
                    } else {
                        Logger.w(TAG, exception, "Authorization failed")
                        invalidateAuth()
                        AuthActionResult.Failed.Error(exception)
                    }
                }
        }.getOrElse { exception ->
            Logger.w(TAG, exception, "Authorization failed with exception")
            AuthActionResult.Failed.Error(exception)
        }
    }

    override suspend fun signOut() {
        if (authenticationState.value != AuthenticationState.Authorized) return

        if (connectivityManager.isNetworkAvailable.not()) {
            invalidateAuth()
            return
        }

        authenticationInProgress.value = true

        val configuration = openIdManager.authConfiguration(REALM_URL).getOrElse {
            invalidateAuth()
            return
        }

        val idToken = authStorage.authState.idToken ?: run {
            invalidateAuth()
            return
        }

        Logger.d(TAG, "Start end session")

        runCatching {
            openIdManager.performEndSessionRequest(configuration, idToken, POST_LOGOUT_REDIRECT_URL)
        }
            .onSuccess {
                Logger.d(TAG, "End session successful")
                clearAuthState()
                authenticationInProgress.value = false
                authenticationState.value = AuthenticationState.Guest
            }
            .onFailure { exception ->
                if (exception is OpenIdManager.UserCancelledException) {
                    Logger.d(TAG, "User cancelled end session")
                    authenticationInProgress.value = false
                } else {
                    Logger.w(TAG, exception, "End session failed")
                    invalidateAuth()
                }
            }
    }

    override suspend fun token(): TokenResponse {
        return when (authenticationState.value) {
            AuthenticationState.Authorized -> getFreshUserAccessToken()
            AuthenticationState.Guest -> TokenResponse.GuestWithoutToken
        }
    }

    private suspend fun getFreshUserAccessToken(): TokenResponse = refreshTokenMutex.withLock {
        val authState = authStorage.authState

        when {
            authState.isAuthorized && authState.needsTokenRefresh.not() -> {
                authState.accessToken?.let {
                    TokenResponse.Token(it)
                } ?: TokenResponse.TokenError(IllegalStateException("Authorized user with null access token"))
            }

            authState.isAuthorized -> {
                Logger.d(TAG, "Auth needs token refresh")
                openIdManager.performActionWithFreshTokens(authState)
                    .verified()
                    .onSuccess {
                        Logger.d(TAG, "Auth refresh token successful")
                        updateAuthState(it)
                    }
                    .mapCatching {
                        TokenResponse.Token(it.accessToken!!)
                    }
                    .onFailure { authorizationException ->
                        Logger.e(TAG, authorizationException, "Auth refresh token error")
                        if (authorizationException.isUnrecoverableException()) {
                            invalidateAuth()
                        }
                    }.getOrElse {
                        TokenResponse.TokenError(it)
                    }
            }

            else -> TokenResponse.TokenError(IllegalStateException("Authorized user without authorized auth state"))
        }
    }

    private fun invalidateAuth() {
        clearAuthState()
        authenticationInProgress.value = false
        authenticationState.value = AuthenticationState.Guest
    }

    private fun updateAuthState(authState: AuthState) {
        Logger.d(TAG, "access token = ${authState.accessToken}")
        Logger.d(TAG, "refresh token = ${authState.refreshToken}")
        Logger.d(TAG, "id token = ${authState.idToken}")
        authStorage.authState = authState
    }

    private fun clearAuthState() {
        authStorage.authState = AuthState()
    }

    private suspend fun Result<AuthState>.verified(): Result<AuthState> = this.flatMap { authState ->
        authState.accessToken?.let { token ->
            tokenVerifier.verifyToken(token)
                .onSuccess { Logger.d(TAG, "Token verified") }
                .map { authState }
        } ?: Result.failure(IllegalStateException("Access token is empty"))
    }

}

private fun Throwable.isUnrecoverableException() = this in listOf(
    AuthorizationException.TokenRequestErrors.INVALID_REQUEST,
    AuthorizationException.TokenRequestErrors.INVALID_CLIENT,
    AuthorizationException.TokenRequestErrors.INVALID_GRANT,
    AuthorizationException.TokenRequestErrors.UNAUTHORIZED_CLIENT,
    AuthorizationException.TokenRequestErrors.UNSUPPORTED_GRANT_TYPE,
    AuthorizationException.TokenRequestErrors.INVALID_SCOPE,
) || this is TokenVerificationException


inline fun <R, T> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> = fold(
    onSuccess = { transform(it) },
    onFailure = { Result.failure(it) },
)
