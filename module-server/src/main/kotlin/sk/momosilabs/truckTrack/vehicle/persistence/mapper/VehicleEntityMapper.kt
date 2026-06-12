package sk.momosilabs.truckTrack.vehicle.persistence.mapper

import sk.momosilabs.truckTrack.vehicle.entity.VehicleEntity
import sk.momosilabs.truckTrack.vehicle.model.VehicleModel

fun VehicleEntity.toModel() = VehicleModel(
    id = id,
    licensePlate = licensePlate,
    make = make,
    model = model,
    type = type,
)
