package com.momosi.trucktrack.core.vehicle.api

import com.momosi.trucktrack.shared.vehicle.VehicleDto
import de.jensklingenberg.ktorfit.http.GET

interface VehicleApi {

    @GET("api/v1/vehicle")
    suspend fun getVehicleList(): List<VehicleDto>
}
