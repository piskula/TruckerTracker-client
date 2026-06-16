package sk.momosilabs.truckTrack.issueManagement.controller

import sk.momosilabs.truckTrack.account.model.AccountModel
import sk.momosilabs.truckTrack.api.issue.dto.AccountDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueFilterDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueHistoryDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueHistoryEventTypeDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssuePriorityDTO
import sk.momosilabs.truckTrack.api.issue.dto.IssueStatusDTO
import sk.momosilabs.truckTrack.api.vehicle.dto.VehicleDTO
import sk.momosilabs.truckTrack.api.vehicle.dto.VehicleTypeDTO
import sk.momosilabs.truckTrack.issueManagement.entity.IssuePriority
import sk.momosilabs.truckTrack.issueManagement.entity.IssueStatus
import sk.momosilabs.truckTrack.issueManagement.model.IssueHistoryModel
import sk.momosilabs.truckTrack.issueManagement.model.IssueModel
import sk.momosilabs.truckTrack.issueManagement.service.IssueListFilter
import sk.momosilabs.truckTrack.vehicle.model.VehicleModel

fun IssueStatusDTO.toModel() = IssueStatus.valueOf(name)
fun IssuePriorityDTO.toModel() = IssuePriority.valueOf(name)

fun IssueModel.toDTO() = IssueDTO(
    id = id,
    title = title,
    description = description,
    status = IssueStatusDTO.valueOf(status.name),
    priority = IssuePriorityDTO.valueOf(priority.name),
    vehicle = vehicle.toDTO(),
    reportedBy = reportedBy.toDTO(),
    assignedTo = assignedTo?.toDTO(),
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun IssueHistoryModel.toDTO() = IssueHistoryDTO(
    id = id,
    type = IssueHistoryEventTypeDTO.valueOf(type.name),
    performedBy = performedBy.toDTO(),
    createdAt = createdAt,
    statusFrom = statusFrom?.let { IssueStatusDTO.valueOf(it.name) },
    statusTo = statusTo?.let { IssueStatusDTO.valueOf(it.name) },
    commentText = commentText,
)

fun VehicleModel.toDTO() = VehicleDTO(
    id = id,
    licensePlate = licensePlate,
    make = make,
    model = model,
    type = VehicleTypeDTO.valueOf(type.name),
)

fun AccountModel.toDTO() = AccountDTO(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
)

fun IssueFilterDTO.toModel() = IssueListFilter(
    statuses = statuses.map { it.toModel() },
    vehicleIds = vehicleIds,
    accountIds = accountIds,
)
