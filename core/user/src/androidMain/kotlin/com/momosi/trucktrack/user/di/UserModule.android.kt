package com.momosi.trucktrack.user.di

import android.content.Context
import com.momosi.trucktrack.user.AuthManager
import com.momosi.trucktrack.user.AuthManagerImpl
import com.momosi.trucktrack.user.UserRepository
import com.momosi.trucktrack.user.UserRepositoryImpl
import com.momosi.trucktrack.user.internal.JwtParser
import com.momosi.trucktrack.user.internal.OpenIdManager
import com.momosi.trucktrack.user.internal.TokenVerifier
import com.momosi.trucktrack.user.internal.USER_AUTH_STORAGE
import com.momosi.trucktrack.user.internal.UserStorage
import com.momosi.trucktrack.user.internal.UserStorageImpl
import com.momosi.trucktrack.user.internal.api.AuthApi
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual fun platformUserModule(): Module = module {
    single(named(USER_AUTH_STORAGE)) {
        get<Context>().getSharedPreferences(USER_AUTH_STORAGE, Context.MODE_PRIVATE)
    }
    single<UserStorage> { UserStorageImpl(get(named(USER_AUTH_STORAGE))) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single { OpenIdManager(get()) }
    single { AuthApi() }
    single { JwtParser(get()) }
    single { TokenVerifier(get(), get(), get(), get()) }
    single<AuthManager> { AuthManagerImpl(get(), get(), get(), get(), get(), get()) }
}
