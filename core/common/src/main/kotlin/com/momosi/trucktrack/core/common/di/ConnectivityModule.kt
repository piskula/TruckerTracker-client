package com.momosi.trucktrack.core.common.di

import android.content.Context
import com.momosi.trucktrack.core.common.network.ConnectivityManager
import com.momosi.trucktrack.core.common.network.ConnectivityManagerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConnectivityModule {

    @Binds
    @Singleton
    abstract fun bindConnectivityManager(impl: ConnectivityManagerImpl): ConnectivityManager

    companion object {
        @Provides
        @Singleton
        fun provideAndroidConnectivityManager(
            @ApplicationContext context: Context,
        ): android.net.ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
    }
}

