package sk.momosilabs.truckTrack.api.dto

import java.time.OffsetDateTime

data class VehicleDTO(
    val id: Long,
    val registrationPlate: String,
    val model: String,
    val lastSeen: OffsetDateTime,
)
