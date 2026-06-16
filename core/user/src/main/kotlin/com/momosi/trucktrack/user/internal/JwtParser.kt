package com.momosi.trucktrack.user.internal

import android.util.Base64
import com.momosi.trucktrack.core.common.coroutines.DispatcherProvider
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.gson.io.GsonDeserializer
import kotlinx.coroutines.withContext
import java.io.Reader
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.inject.Inject

class JwtParser @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
) {

    private lateinit var payload: String

    suspend fun verifyAndGetClaims(jwtToken: String, verifyingKey: String): Jws<Claims> = withContext(dispatcherProvider.io()) {
        Jwts.parser()
            .verifyWith(verifyingKey.toPublicKey())
            .build()
            .parseSignedClaims(jwtToken)
    }

    suspend fun verifyAndGetBody(jwtToken: String, verifyingKey: String): String {
        return withContext(dispatcherProvider.io()) {
            payload = ""

            // create jwt parser
            val jws = Jwts.parser()
                .verifyWith(verifyingKey.toPublicKey())
                .json(CustomDeserializer())
                .build()

            // validate token
            // we are not interested in parsed headers and body so use only payload
            jws.parse(jwtToken)

            return@withContext payload
        }
    }

    private fun String.toPublicKey() = KeyFactory
        .getInstance("RSA")
        .generatePublic(X509EncodedKeySpec(Base64.decode(this, Base64.DEFAULT)))

    // payload is not accessible via library interface so we have to store manually when parsing results
    // I tried a few others but they don't work on Android at all or on older Android versions
    inner class CustomDeserializer : GsonDeserializer<Map<String, *>>() {

        private var headerProcessed = false

        override fun doDeserialize(reader: Reader?): Map<String, *> {
            if (headerProcessed) {
                reader?.use {
                    payload = it.readText()
                }
            }
            headerProcessed = true // first deserialize is for header, another for payload
            return super.doDeserialize(reader)
        }
    }
}
