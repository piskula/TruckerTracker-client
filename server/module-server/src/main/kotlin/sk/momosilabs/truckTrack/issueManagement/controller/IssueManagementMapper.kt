@file:OptIn(ExperimentalUuidApi::class)

package sk.momosilabs.truckTrack.issueManagement.controller

import com.momosi.trucktrack.shared.issue.AccountDto
import com.momosi.trucktrack.shared.issue.IssueDto
import com.momosi.trucktrack.shared.issue.IssueFilterDto
import com.momosi.trucktrack.shared.issue.IssueHistoryDto
import com.momosi.trucktrack.shared.issue.IssueHistoryEventTypeDto
import com.momosi.trucktrack.shared.issue.IssuePriorityDto
import com.momosi.trucktrack.shared.issue.IssueStatusDto
import com.momosi.trucktrack.shared.vehicle.VehicleDto
import com.momosi.trucktrack.shared.vehicle.VehicleTypeDto
import kotlin.time.toKotlinInstant
import sk.momosilabs.truckTrack.account.model.AccountModel
import sk.momosilabs.truckTrack.issueManagement.entity.IssuePriority
import sk.momosilabs.truckTrack.issueManagement.entity.IssueStatus
import sk.momosilabs.truckTrack.issueManagement.model.IssueHistoryModel
import sk.momosilabs.truckTrack.issueManagement.model.IssueModel
import sk.momosilabs.truckTrack.issueManagement.service.IssueListFilter
import sk.momosilabs.truckTrack.vehicle.model.VehicleModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid

fun IssueStatusDto.toModel() = IssueStatus.valueOf(name)
fun IssuePriorityDto.toModel() = IssuePriority.valueOf(name)

fun IssueModel.toDto() = IssueDto(
    id = id,
    title = title,
    description = description,
    status = IssueStatusDto.valueOf(status.name),
    priority = IssuePriorityDto.valueOf(priority.name),
    vehicle = vehicle.toDto(),
    reportedBy = reportedBy.toDto(),
    assignedTo = assignedTo?.toDto(),
    createdAt = createdAt.toInstant().toKotlinInstant(),
    updatedAt = updatedAt.toInstant().toKotlinInstant(),
)

fun IssueHistoryModel.toDto() = IssueHistoryDto(
    id = id.toKotlinUuid(),
    type = IssueHistoryEventTypeDto.valueOf(type.name),
    performedBy = performedBy.toDto(),
    createdAt = createdAt.toInstant().toKotlinInstant(),
    statusFrom = statusFrom?.let { IssueStatusDto.valueOf(it.name) },
    statusTo = statusTo?.let { IssueStatusDto.valueOf(it.name) },
    commentText = commentText,
)

fun VehicleModel.toDto() = VehicleDto(
    id = id,
    licensePlate = licensePlate,
    make = make,
    model = model,
    type = VehicleTypeDto.valueOf(type.name),
)

fun AccountModel.toDto() = AccountDto(
    id = id.toKotlinUuid(),
    username = username,
    firstName = firstName,
    lastName = lastName,
)

fun IssueFilterDto.toModel() = IssueListFilter(
    statuses = statuses.map { it.toModel() },
    vehicleIds = vehicleIds,
    accountIds = accountIds.map { it.toJavaUuid() },
)
