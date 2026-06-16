package com.momosi.trucktrack.core.vehicle.dto

import kotlinx.serialization.Serializable

@Serializable
data class VehicleDto(
    val id: Long = 0,
    val licensePlate: String = "",
    val make: String = "",
    val model: String = "",
    val type: String = "",
)

