package sk.momosilabs.truckTrack.vehicle.controller

import org.springframework.web.bind.annotation.RestController
import sk.momosilabs.truckTrack.api.vehicle.VehicleApi
import sk.momosilabs.truckTrack.api.vehicle.dto.VehicleDTO
import sk.momosilabs.truckTrack.api.vehicle.dto.VehicleTypeDTO
import sk.momosilabs.truckTrack.vehicle.model.VehicleModel
import sk.momosilabs.truckTrack.vehicle.service.getVehicleList.GetVehicleListUseCase

@RestController
class VehicleController(
    private val getVehicleList: GetVehicleListUseCase,
) : VehicleApi {

    override fun getVehicleList(): List<VehicleDTO> =
        getVehicleList.get().map { it.toDTO() }
}

private fun VehicleModel.toDTO() = VehicleDTO(
    id = id,
    licensePlate = licensePlate,
    make = make,
    model = model,
    type = VehicleTypeDTO.valueOf(type.name),
)
