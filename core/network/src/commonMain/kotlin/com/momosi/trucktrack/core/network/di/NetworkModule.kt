package com.momosi.trucktrack.core.network.di

import com.momosi.trucktrack.core.common.TruckTrackConfig
import com.momosi.trucktrack.core.common.logger.Logger
import com.momosi.trucktrack.core.network.httpClientEngineFactory
import com.momosi.trucktrack.user.AuthManager
import com.momosi.trucktrack.user.model.TokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single {
        val json: Json = get()
        val authManager: AuthManager = get()
        buildHttpClient(authManager) {
            defaultRequest {
                url(TruckTrackConfig.API_BASE_URL)
                contentType(ContentType.Application.Json)
            }
            install(ContentNegotiation) {
                json(json)
            }
        }
    }

    single(named("image")) {
        val authManager: AuthManager = get()
        buildHttpClient(authManager) {
            defaultRequest {
                url(TruckTrackConfig.API_BASE_URL)
            }
        }
    }
}

private fun buildHttpClient(authManager: AuthManager, block: HttpClientConfig<*>.() -> Unit = {}): HttpClient = HttpClient(httpClientEngineFactory()) {
    block()

    install(Auth) {
        bearer {
            loadTokens {
                authManager.token().toBearerTokens()
            }
            refreshTokens {
                authManager.token().toBearerTokens()
            }
        }
    }

    install(Logging) {
        level = LogLevel.BODY
        logger = object : io.ktor.client.plugins.logging.Logger {
            override fun log(message: String) {
                Logger.d("Ktor", message)
            }
        }
    }
}

private fun TokenResponse.toBearerTokens(): BearerTokens? = when (this) {
    is TokenResponse.Token -> BearerTokens(accessToken = token, refreshToken = "")
    else -> null
}
