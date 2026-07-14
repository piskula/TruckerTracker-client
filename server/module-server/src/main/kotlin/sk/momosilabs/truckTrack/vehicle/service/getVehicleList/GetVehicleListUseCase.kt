package sk.momosilabs.truckTrack.vehicle.service.getVehicleList

import sk.momosilabs.truckTrack.vehicle.model.VehicleModel

interface GetVehicleListUseCase {

    fun get(): List<VehicleModel>
}
