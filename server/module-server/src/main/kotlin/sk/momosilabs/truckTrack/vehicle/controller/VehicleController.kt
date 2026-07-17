package sk.momosilabs.truckTrack.vehicle.controller

import sk.momosilabs.truckTrack.api.vehicle.dto.VehicleDto
import sk.momosilabs.truckTrack.api.vehicle.dto.VehicleTypeDto
import org.springframework.web.bind.annotation.RestController
import sk.momosilabs.truckTrack.api.vehicle.VehicleApi
import sk.momosilabs.truckTrack.vehicle.model.VehicleModel
import sk.momosilabs.truckTrack.vehicle.service.getVehicleList.GetVehicleListUseCase

@RestController
class VehicleController(
    private val getVehicleList: GetVehicleListUseCase,
) : VehicleApi {

    override fun getVehicleList(): List<VehicleDto> =
        getVehicleList.get().map { it.toDto() }
}

private fun VehicleModel.toDto() = VehicleDto(
    id = id,
    licensePlate = licensePlate,
    make = make,
    model = model,
    type = VehicleTypeDto.valueOf(type.name),
)
