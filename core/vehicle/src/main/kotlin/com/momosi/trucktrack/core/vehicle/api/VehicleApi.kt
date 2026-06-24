package com.momosi.trucktrack.core.vehicle.api

import com.momosi.trucktrack.core.vehicle.dto.VehicleDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VehicleApi @Inject constructor(private val client: HttpClient) {

    suspend fun getVehicleList(): List<VehicleDto> =
        client.get("api/v1/vehicle").body()
}
