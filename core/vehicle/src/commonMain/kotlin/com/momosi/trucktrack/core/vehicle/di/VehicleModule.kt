package com.momosi.trucktrack.core.vehicle.di

import com.momosi.trucktrack.core.vehicle.VehicleRepository
import com.momosi.trucktrack.core.vehicle.VehicleRepositoryImpl
import com.momosi.trucktrack.core.vehicle.api.VehicleApi
import org.koin.dsl.module

val vehicleModule = module {
    single { VehicleApi(get()) }
    single<VehicleRepository> { VehicleRepositoryImpl(get()) }
}
