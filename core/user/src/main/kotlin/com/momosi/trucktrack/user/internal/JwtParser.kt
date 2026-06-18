package com.momosi.trucktrack.user.internal

import android.util.Base64
import com.momosi.trucktrack.core.common.coroutines.DispatcherProvider
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import kotlinx.coroutines.withContext
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.inject.Inject

class JwtParser @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
) {

    suspend fun verifyAndGetClaims(jwtToken: String, verifyingKey: String): Jws<Claims> = withContext(dispatcherProvider.io()) {
        Jwts.parser()
            .verifyWith(verifyingKey.toPublicKey())
            .build()
            .parseSignedClaims(jwtToken)
    }

    private fun String.toPublicKey() = KeyFactory
        .getInstance("RSA")
        .generatePublic(X509EncodedKeySpec(Base64.decode(this, Base64.DEFAULT)))
}
