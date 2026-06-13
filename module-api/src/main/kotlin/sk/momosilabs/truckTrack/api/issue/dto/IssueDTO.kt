package sk.momosilabs.truckTrack.api.issue.dto

import sk.momosilabs.truckTrack.api.vehicle.dto.VehicleDTO
import java.time.OffsetDateTime

data class IssueDTO(
    val id: Long,
    val title: String,
    val description: String,
    val status: IssueStatusDTO,
    val priority: IssuePriorityDTO,
    val vehicle: VehicleDTO,
    val reportedBy: AccountDTO,
    val assignedTo: AccountDTO?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
