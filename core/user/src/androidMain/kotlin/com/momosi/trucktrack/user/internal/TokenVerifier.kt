package com.momosi.trucktrack.user.internal

import com.momosi.trucktrack.core.common.coroutines.DispatcherProvider
import com.momosi.trucktrack.core.common.logger.Logger
import com.momosi.trucktrack.user.internal.api.AuthApi
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws

data class TokenVerificationException(override val message: String? = null, override val cause: Throwable? = null) : RuntimeException(message, cause)

class TokenVerifier(private val jwtParser: JwtParser, private val userStorage: UserStorage, private val authApi: AuthApi, private val dispatcherProvider: DispatcherProvider) {

    internal suspend fun verifyToken(accessToken: String, refreshPublicKey: Boolean = false): Result<Jws<Claims>> = with(dispatcherProvider.io()) {
        val currentPublicKey = userStorage.serverPublicKey
        val publicKey = if (refreshPublicKey || currentPublicKey == null) {
            runCatching {
                authApi.getRealmInfo().publicKey
            }.onSuccess {
                userStorage.serverPublicKey = it
            }.onFailure {
                Logger.e("TokenVerifier", it, "Error fetching server public key")
            }.getOrNull()
        } else {
            currentPublicKey
        }

        return if (publicKey == null) {
            Result.failure(TokenVerificationException("Public key for token verification is not available"))
        } else {
            runCatching {
                jwtParser.verifyAndGetClaims(accessToken, publicKey)
            }.recoverWith {
                if (!refreshPublicKey) {
                    verifyToken(accessToken, refreshPublicKey = true)
                } else {
                    Result.failure(TokenVerificationException(cause = it))
                }
            }
        }
    }
}

inline fun <T> Result<T>.recoverWith(transform: (Throwable) -> Result<T>): Result<T> = fold(
    onSuccess = { this },
    onFailure = { transform(it) },
)
