package sk.momosilabs.truckTrack.issueManagement.model

import sk.momosilabs.truckTrack.account.model.AccountModel
import sk.momosilabs.truckTrack.issueManagement.entity.IssuePriority
import sk.momosilabs.truckTrack.issueManagement.entity.IssueStatus
import sk.momosilabs.truckTrack.vehicle.model.VehicleModel
import java.time.OffsetDateTime

data class IssueModel(
    val id: Long = 0L,
    val title: String,
    val description: String,
    val status: IssueStatus,
    val priority: IssuePriority,
    val vehicle: VehicleModel,
    val reportedBy: AccountModel,
    val assignedTo: AccountModel?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
