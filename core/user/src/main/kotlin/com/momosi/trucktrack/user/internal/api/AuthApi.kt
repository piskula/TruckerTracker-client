package com.momosi.trucktrack.user.internal.api

import com.momosi.trucktrack.core.common.TruckTrackConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class AuthApi {

    private val client: HttpClient by lazy {
        HttpClient(OkHttp) {
            defaultRequest {
                url(TruckTrackConfig.REALM_URL)
            }
            install(ContentNegotiation) {
                json(
                    Json { ignoreUnknownKeys = true },
                )
            }
        }
    }

    suspend fun getRealm(): RealmDto = client.get("./").body()
}
