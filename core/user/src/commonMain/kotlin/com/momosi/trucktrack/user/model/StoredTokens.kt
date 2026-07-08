package com.momosi.trucktrack.user.model

import kotlinx.serialization.Serializable
import org.publicvalue.multiplatform.oidc.types.remote.AccessTokenResponse
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val REFRESH_BUFFER_SECONDS = 30L

@Serializable
data class StoredTokens(val accessToken: String, val refreshToken: String?, val idToken: String?, val expiresIn: Int, val receivedAt: Long) {
    @OptIn(ExperimentalTime::class)
    fun needsRefresh(): Boolean = Clock.System.now().epochSeconds >= (receivedAt + expiresIn - REFRESH_BUFFER_SECONDS)
}

@OptIn(ExperimentalTime::class)
fun AccessTokenResponse.toStoredTokens(): StoredTokens = StoredTokens(
    accessToken = access_token,
    refreshToken = refresh_token,
    idToken = id_token,
    expiresIn = expires_in ?: 0,
    receivedAt = received_at,
)
