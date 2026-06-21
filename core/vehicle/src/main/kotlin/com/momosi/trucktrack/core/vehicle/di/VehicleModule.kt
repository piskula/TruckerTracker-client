package com.momosi.trucktrack.core.vehicle.di

import com.momosi.trucktrack.core.vehicle.VehicleRepository
import com.momosi.trucktrack.core.vehicle.VehicleRepositoryImpl
import com.momosi.trucktrack.core.vehicle.api.VehicleApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class VehicleModule {

    @Binds
    @Singleton
    abstract fun bindVehicleRepository(impl: VehicleRepositoryImpl): VehicleRepository

    companion object {

        @Provides
        @Singleton
        fun provideVehicleApi(retrofit: Retrofit): VehicleApi = retrofit.create(VehicleApi::class.java)
    }
}
