package sk.momosilabs.truckTrack.vehicle.service.getVehicleList

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.momosilabs.truckTrack.vehicle.model.VehicleModel
import sk.momosilabs.truckTrack.vehicle.service.VehiclePersistence

@Service
class GetVehicleList(
    private val vehiclePersistence: VehiclePersistence,
) : GetVehicleListUseCase {

    @Transactional(readOnly = true)
    override fun get(): List<VehicleModel> =
        vehiclePersistence.findAll()
}
