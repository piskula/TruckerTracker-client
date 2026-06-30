package com.momosi.trucktrack.core.vehicle

import com.momosi.trucktrack.core.vehicle.api.VehicleApi
import com.momosi.trucktrack.core.vehicle.dto.toVehicle
import com.momosi.trucktrack.core.vehicle.model.Vehicle

class VehicleRepositoryImpl(private val vehicleApi: VehicleApi) : VehicleRepository {

    override suspend fun getVehicles(): Result<List<Vehicle>> = runCatching {
        vehicleApi.getVehicleList().map { it.toVehicle() }
    }
}
