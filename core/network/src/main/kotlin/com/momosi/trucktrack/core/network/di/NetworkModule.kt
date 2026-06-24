package com.momosi.trucktrack.core.network.di

import com.momosi.trucktrack.core.common.TruckTrackConfig
import com.momosi.trucktrack.core.common.logger.Logger
import com.momosi.trucktrack.user.AuthManager
import com.momosi.trucktrack.user.model.TokenResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
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
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideHttpClient(json: Json, authManager: AuthManager): HttpClient = HttpClient(OkHttp) {
        defaultRequest {
            url(TruckTrackConfig.API_BASE_URL)
            contentType(ContentType.Application.Json)
        }

        install(ContentNegotiation) {
            json(json)
        }

        install(Auth) {
            bearer {
                loadTokens {
                    when (val tokenResponse = authManager.token()) {
                        is TokenResponse.Token -> BearerTokens(
                            accessToken = tokenResponse.token,
                            refreshToken = "",
                        )

                        else -> null
                    }
                }

                refreshTokens {
                    when (val tokenResponse = authManager.token()) {
                        is TokenResponse.Token -> BearerTokens(
                            accessToken = tokenResponse.token,
                            refreshToken = "",
                        )

                        else -> null
                    }
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
}
