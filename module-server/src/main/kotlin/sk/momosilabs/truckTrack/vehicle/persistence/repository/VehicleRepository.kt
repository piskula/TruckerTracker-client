package sk.momosilabs.truckTrack.vehicle.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import sk.momosilabs.truckTrack.vehicle.entity.VehicleEntity

interface VehicleRepository : JpaRepository<VehicleEntity, Long>
