package com.momosi.trucktrack.core.vehicle.dto

import com.momosi.trucktrack.core.vehicle.model.Vehicle
import com.momosi.trucktrack.core.vehicle.model.VehicleType

fun VehicleDto.toVehicle(): Vehicle = Vehicle(
    id = id,
    licensePlate = licensePlate,
    make = make,
    model = model,
    type = VehicleType.fromApiValue(type),
)

