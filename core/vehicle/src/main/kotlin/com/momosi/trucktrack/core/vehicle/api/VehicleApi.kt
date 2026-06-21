package com.momosi.trucktrack.core.vehicle.api

import com.momosi.trucktrack.core.vehicle.dto.VehicleDto
import retrofit2.http.GET

interface VehicleApi {

    @GET("api/v1/vehicle")
    suspend fun getVehicleList(): List<VehicleDto>
}
