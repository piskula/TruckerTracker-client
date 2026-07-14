package sk.momosilabs.truckTrack.issueManagement.persistence.mapper

import sk.momosilabs.truckTrack.account.persistence.mapper.toModel
import sk.momosilabs.truckTrack.issueManagement.entity.IssueHistoryEntity
import sk.momosilabs.truckTrack.issueManagement.model.IssueHistoryModel
import sk.momosilabs.truckTrack.util.toUtcOffsetDateTime

fun IssueHistoryEntity.toModel() = IssueHistoryModel(
    id = id,
    issueId = issue.id,
    type = type,
    performedBy = performedBy.toModel(),
    createdAt = createdAtUtc.toUtcOffsetDateTime(),
    statusFrom = statusFrom,
    statusTo = statusTo,
    commentText = commentText,
)
