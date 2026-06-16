package com.momosi.trucktrack.core.vehicle

import com.momosi.trucktrack.core.vehicle.model.Vehicle

interface VehicleRepository {

    suspend fun getVehicles(): Result<List<Vehicle>>
}

