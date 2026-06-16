package com.momosi.trucktrack.core.network.di

import com.momosi.trucktrack.core.common.network.ConnectivityManager
import com.momosi.trucktrack.user.AuthManager
import com.momosi.trucktrack.user.internal.networking.UserAuthenticator
import com.momosi.trucktrack.user.internal.networking.UserAuthorizationInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://tt.momosi.org/"

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
    fun provideOkHttpClient(
        authManager: AuthManager,
        connectivityManager: ConnectivityManager,
        userAuthenticator: UserAuthenticator,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(UserAuthorizationInterceptor(authManager, connectivityManager))
        .authenticator(userAuthenticator)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            },
        )
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}

