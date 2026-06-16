package com.momosi.trucktrack.core.vehicle.model

enum class VehicleType {
    Truck,
    Trailer,
    ;

    companion object {
        fun fromApiValue(value: String): VehicleType = when (value) {
            "TRUCK" -> Truck
            "TRAILER" -> Trailer
            else -> Truck
        }
    }
}

