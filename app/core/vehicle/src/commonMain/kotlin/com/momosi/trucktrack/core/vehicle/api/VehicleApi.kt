package com.momosi.trucktrack.core.vehicle.api

import com.momosi.trucktrack.shared.vehicle.VehicleDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class VehicleApi(private val client: HttpClient) {

    suspend fun getVehicleList(): List<VehicleDto> = client.get("api/v1/vehicle").body()
}
