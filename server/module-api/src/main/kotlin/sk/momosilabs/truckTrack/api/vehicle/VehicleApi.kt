package sk.momosilabs.truckTrack.api.vehicle

import sk.momosilabs.truckTrack.api.vehicle.dto.VehicleDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping

@Tag(name = "Vehicles")
interface VehicleApi {

    companion object {
        private const val ENDPOINT = "/api/v1/vehicle"
    }

    @Operation(summary = "Get vehicle list")
    @GetMapping(ENDPOINT)
    fun getVehicleList(): List<VehicleDto>
}
