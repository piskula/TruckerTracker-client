package com.momosi.trucktrack.core.vehicle.di

import com.momosi.trucktrack.core.vehicle.VehicleRepository
import com.momosi.trucktrack.core.vehicle.VehicleRepositoryImpl
import com.momosi.trucktrack.core.vehicle.api.createVehicleApi
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.dsl.module

val vehicleModule = module {
    single { get<Ktorfit>().createVehicleApi() }
    single<VehicleRepository> { VehicleRepositoryImpl(get()) }
}
