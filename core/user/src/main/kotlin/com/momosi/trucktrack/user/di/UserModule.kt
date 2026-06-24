package com.momosi.trucktrack.user.di

import android.content.Context
import android.content.SharedPreferences
import com.momosi.trucktrack.user.AuthManager
import com.momosi.trucktrack.user.AuthManagerImpl
import com.momosi.trucktrack.user.UserRepository
import com.momosi.trucktrack.user.UserRepositoryImpl
import com.momosi.trucktrack.user.internal.USER_AUTH_STORAGE
import com.momosi.trucktrack.user.internal.UserStorage
import com.momosi.trucktrack.user.internal.UserStorageImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
        fun provideAuthSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
            context.getSharedPreferences(USER_AUTH_STORAGE, Context.MODE_PRIVATE)
    }
}
