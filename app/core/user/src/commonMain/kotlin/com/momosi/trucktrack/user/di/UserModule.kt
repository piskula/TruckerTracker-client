package com.momosi.trucktrack.user.di

import com.momosi.trucktrack.core.common.TruckTrackConfig
import com.momosi.trucktrack.user.AuthManager
import com.momosi.trucktrack.user.AuthManagerImpl
import com.momosi.trucktrack.user.UserRepository
import com.momosi.trucktrack.user.UserRepositoryImpl
import com.momosi.trucktrack.user.internal.TokenVerifier
import com.momosi.trucktrack.user.internal.api.AuthApi
import org.koin.core.module.Module
import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.OpenIdConnectClient

expect fun platformUserModule(): Module

val userModule = module {
    single {
        OpenIdConnectClient(discoveryUri = "${TruckTrackConfig.REALM_URL}.well-known/openid-configuration") {
            clientId = TruckTrackConfig.OAUTH_CLIENT_ID
            scope = "openid offline_access"
            redirectUri = TruckTrackConfig.OAUTH_REDIRECT_URL
            postLogoutRedirectUri = TruckTrackConfig.OAUTH_LOGOUT_REDIRECT_URL
        }
    }
    single { AuthApi() }
    single { TokenVerifier(get(), get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<AuthManager> { AuthManagerImpl(get(), get(), get(), get(), get(), get()) }
}
