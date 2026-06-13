package sk.momosilabs.truckTrack.api.issue.dto

import java.time.OffsetDateTime
import java.util.UUID

data class IssueHistoryDTO(
    val id: UUID,
    val type: IssueHistoryEventTypeDTO,
    val performedBy: AccountDTO,
    val createdAt: OffsetDateTime,
    val statusFrom: IssueStatusDTO?,
    val statusTo: IssueStatusDTO?,
    val commentText: String?,
)
