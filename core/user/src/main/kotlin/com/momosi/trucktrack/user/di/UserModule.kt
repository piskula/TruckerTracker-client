package com.momosi.trucktrack.user.di

import android.content.Context
import android.content.SharedPreferences
import com.momosi.trucktrack.user.AuthManager
import com.momosi.trucktrack.user.AuthManagerImpl
import com.momosi.trucktrack.user.REALM_URL
import com.momosi.trucktrack.user.UserRepository
import com.momosi.trucktrack.user.UserRepositoryImpl
import com.momosi.trucktrack.user.internal.USER_AUTH_STORAGE
import com.momosi.trucktrack.user.internal.UserStorage
import com.momosi.trucktrack.user.internal.UserStorageImpl
import com.momosi.trucktrack.user.internal.api.AuthApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {

    @Binds
    @Singleton
    abstract fun bindUserAuthManager(impl: AuthManagerImpl): AuthManager

    @Binds
    @Singleton
    abstract fun bindUserAuthStorage(impl: UserStorageImpl): UserStorage

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    companion object {

        @Provides
        @Singleton
        @Named(USER_AUTH_STORAGE)
        fun provideAuthSharedPreferences(@ApplicationContext context: Context): SharedPreferences = context.getSharedPreferences(USER_AUTH_STORAGE, Context.MODE_PRIVATE)

        @Provides
        @Singleton
        @Named("auth")
        fun provideAuthRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(REALM_URL)
            .addConverterFactory(
                Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()),
            )
            .build()

        @Provides
        @Singleton
        fun provideAuthApi(@Named("auth") retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)
    }
}
