package com.momosi.trucktrack.user.di

import android.content.Context
import com.momosi.trucktrack.user.internal.USER_AUTH_STORAGE
import com.momosi.trucktrack.user.internal.UserStorage
import com.momosi.trucktrack.user.internal.UserStorageImpl
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.appsupport.AndroidCodeAuthFlowFactory
import org.publicvalue.multiplatform.oidc.flows.CodeAuthFlowFactory

actual fun platformUserModule(): Module = module {
    single(named(USER_AUTH_STORAGE)) {
        get<Context>().getSharedPreferences(USER_AUTH_STORAGE, Context.MODE_PRIVATE)
    }
    single<UserStorage> { UserStorageImpl(get(named(USER_AUTH_STORAGE))) }
    single { AndroidCodeAuthFlowFactory() } bind CodeAuthFlowFactory::class
}
