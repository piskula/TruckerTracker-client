package com.momosi.trucktrack.core.vehicle.dto

import com.momosi.trucktrack.core.vehicle.model.Vehicle
import com.momosi.trucktrack.core.vehicle.model.VehicleType
import com.momosi.trucktrack.shared.vehicle.VehicleDto

fun VehicleDto.toVehicle(): Vehicle = Vehicle(
    id = id,
    licensePlate = licensePlate,
    make = make,
    model = model,
    type = VehicleType.fromApiValue(type.name),
)
