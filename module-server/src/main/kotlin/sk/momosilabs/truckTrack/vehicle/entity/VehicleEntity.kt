package sk.momosilabs.truckTrack.vehicle.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.NotNull

@Entity(name = "vehicle")
class VehicleEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @field:NotNull
    var licensePlate: String,

    @field:NotNull
    var make: String,

    @field:NotNull
    var model: String,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    var type: VehicleType,

)
