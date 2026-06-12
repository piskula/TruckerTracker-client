package sk.momosilabs.truckTrack.issue.persistence.mapper

import sk.momosilabs.truckTrack.account.persistence.mapper.toModel
import sk.momosilabs.truckTrack.issue.entity.IssueEntity
import sk.momosilabs.truckTrack.issue.model.IssueModel
import sk.momosilabs.truckTrack.vehicle.persistence.mapper.toModel
import java.time.ZoneOffset

fun IssueEntity.toModel() = IssueModel(
    id = id,
    title = title,
    description = description,
    status = status,
    priority = priority,
    vehicle = vehicle.toModel(),
    reportedBy = reportedBy.toModel(),
    assignedTo = assignedTo?.toModel(),
    createdAt = createdAtUtc.atOffset(ZoneOffset.UTC),
    updatedAt = updatedAtUtc.atOffset(ZoneOffset.UTC),
)
