package sk.momosilabs.truckTrack.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import sk.momosilabs.truckTrack.api.dto.VehicleDTO

@Tag(name = "Vehicles", description = "Vehicle management")
@RequestMapping("/api/v1/vehicles")
interface VehicleApi {

    @Operation(summary = "Get vehicle by ID")
    @GetMapping("/{id}")
    fun getVehicle(@PathVariable id: Long): VehicleDTO

    @Operation(summary = "List all vehicles")
    @GetMapping
    fun listVehicles(): List<VehicleDTO>
}
