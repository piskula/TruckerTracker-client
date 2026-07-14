package com.momosi.trucktrack.shared.vehicle

import kotlinx.serialization.Serializable

@Serializable
data class VehicleDto(
    val id: Long,
    val licensePlate: String,
    val make: String,
    val model: String,
    val type: VehicleTypeDto,
)
