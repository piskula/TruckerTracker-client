package com.momosi.trucktrack.core.vehicle.model

data class Vehicle(
    val id: Long,
    val licensePlate: String,
    val make: String,
    val model: String,
    val type: VehicleType,
)

