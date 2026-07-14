package sk.momosilabs.truckTrack.api.vehicle

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import sk.momosilabs.truckTrack.api.vehicle.dto.VehicleDTO

@Tag(name = "Vehicles")
interface VehicleApi {

    companion object {
        private const val ENDPOINT = "/api/v1/vehicle"
    }

    @Operation(summary = "Get vehicle list")
    @GetMapping(ENDPOINT)
    fun getVehicleList(): List<VehicleDTO>
}
