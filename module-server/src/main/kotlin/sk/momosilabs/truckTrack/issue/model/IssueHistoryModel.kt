package sk.momosilabs.truckTrack.issue.model

import sk.momosilabs.truckTrack.account.model.AccountModel
import sk.momosilabs.truckTrack.issue.entity.IssueHistoryEventType
import sk.momosilabs.truckTrack.issue.entity.IssueStatus
import java.time.OffsetDateTime
import java.util.UUID

data class IssueHistoryModel(
    val id: UUID = UUID(0, 0),
    val issueId: Long,
    val type: IssueHistoryEventType,
    val performedBy: AccountModel,
    val createdAt: OffsetDateTime,
    val statusFrom: IssueStatus?,
    val statusTo: IssueStatus?,
    val commentText: String?,
)
