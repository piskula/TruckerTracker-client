package sk.momosilabs.truckTrack.controller

import org.springframework.web.bind.annotation.RestController
import sk.momosilabs.truckTrack.api.VehicleApi
import sk.momosilabs.truckTrack.api.dto.VehicleDTO
import java.time.OffsetDateTime

@RestController
class VehicleController : VehicleApi {

    override fun getVehicle(id: Long): VehicleDTO = VehicleDTO(
        id = id,
        registrationPlate = "BA-123-AB",
        model = "Mercedes Actros",
        lastSeen = OffsetDateTime.now(),
    )

    override fun listVehicles(): List<VehicleDTO> = listOf(
        VehicleDTO(1L, "BA-123-AB", "Mercedes Actros", OffsetDateTime.now()),
        VehicleDTO(2L, "BA-456-CD", "Volvo FH", OffsetDateTime.now().minusHours(2)),
    )
}
