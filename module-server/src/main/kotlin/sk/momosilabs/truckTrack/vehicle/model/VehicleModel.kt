package sk.momosilabs.truckTrack.vehicle.model

import sk.momosilabs.truckTrack.vehicle.entity.VehicleType

data class VehicleModel(
    val id: Long,
    val licensePlate: String,
    val make: String,
    val model: String,
    val type: VehicleType,
)
