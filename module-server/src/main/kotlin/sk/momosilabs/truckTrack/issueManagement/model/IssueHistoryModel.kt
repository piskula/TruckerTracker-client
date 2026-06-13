package sk.momosilabs.truckTrack.issueManagement.model

import sk.momosilabs.truckTrack.account.model.AccountModel
import sk.momosilabs.truckTrack.issueManagement.entity.IssueHistoryEventType
import sk.momosilabs.truckTrack.issueManagement.entity.IssueStatus
import java.time.OffsetDateTime
import java.util.UUID

data class IssueHistoryModel(
    val id: UUID,
    val issueId: Long,
    val type: IssueHistoryEventType,
    val performedBy: AccountModel,
    val createdAt: OffsetDateTime,
    val statusFrom: IssueStatus?,
    val statusTo: IssueStatus?,
    val commentText: String?,
)
