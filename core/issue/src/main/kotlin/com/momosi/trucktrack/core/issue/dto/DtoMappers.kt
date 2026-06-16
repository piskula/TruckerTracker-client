package com.momosi.trucktrack.core.issue.dto

import com.momosi.trucktrack.core.issue.model.Account
import com.momosi.trucktrack.core.issue.model.Issue
import com.momosi.trucktrack.core.issue.model.IssueAttachment
import com.momosi.trucktrack.core.issue.model.IssueCreate
import com.momosi.trucktrack.core.issue.model.IssueHistory
import com.momosi.trucktrack.core.issue.model.IssueHistoryType
import com.momosi.trucktrack.core.issue.model.IssuePriority
import com.momosi.trucktrack.core.issue.model.IssueStatus
import com.momosi.trucktrack.core.vehicle.dto.toVehicle
import java.time.Instant

internal fun AccountDto.toAccount(): Account = Account(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
)

internal fun IssueDto.toIssue(): Issue = Issue(
    id = id,
    title = title,
    description = description,
    status = IssueStatus.fromApiValue(status),
    priority = IssuePriority.fromApiValue(priority),
    vehicle = vehicle?.toVehicle(),
    reportedBy = reportedBy?.toAccount(),
    assignedTo = assignedTo?.toAccount(),
    createdAt = createdAt.parseInstant(),
    updatedAt = updatedAt.parseInstant(),
)

internal fun IssueCreate.toDto(): IssueCreateDto = IssueCreateDto(
    vehicleId = vehicleId,
    title = title,
    description = description,
    priority = priority.toApiValue(),
)

internal fun IssueAttachmentDto.toIssueAttachment(): IssueAttachment = IssueAttachment(
    id = id,
    filename = filename,
    contentType = contentType,
    sizeBytes = sizeBytes,
    uploadedBy = uploadedBy?.toAccount(),
    uploadedAt = uploadedAt.parseInstant(),
)

internal fun IssueHistoryDto.toIssueHistory(): IssueHistory = IssueHistory(
    id = id,
    type = IssueHistoryType.fromApiValue(type),
    performedBy = performedBy?.toAccount(),
    createdAt = createdAt.parseInstant(),
    statusFrom = statusFrom?.let { IssueStatus.fromApiValue(it) },
    statusTo = statusTo?.let { IssueStatus.fromApiValue(it) },
    commentText = commentText,
)

private fun String.parseInstant(): Instant = runCatching {
    Instant.parse(this)
}.getOrDefault(Instant.EPOCH)

