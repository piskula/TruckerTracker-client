package sk.momosilabs.truckTrack.issue.persistence.mapper

import sk.momosilabs.truckTrack.account.persistence.mapper.toModel
import sk.momosilabs.truckTrack.issue.entity.IssueHistoryEntity
import sk.momosilabs.truckTrack.issue.model.IssueHistoryModel
import java.time.ZoneOffset

fun IssueHistoryEntity.toModel() = IssueHistoryModel(
    id = id,
    issueId = issue.id,
    type = type,
    performedBy = performedBy.toModel(),
    createdAt = createdAtUtc.atOffset(ZoneOffset.UTC),
    statusFrom = statusFrom,
    statusTo = statusTo,
    commentText = commentText,
)
