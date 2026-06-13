package sk.momosilabs.truckTrack.api.vehicle.dto

data class VehicleDTO(
    val id: Long,
    val licensePlate: String,
    val make: String,
    val model: String,
    val type: VehicleTypeDTO,
)
