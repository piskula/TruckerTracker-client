package sk.momosilabs.truckTrack.api.issue.dto

import java.time.OffsetDateTime
import java.util.UUID

data class IssueHistoryDto(
    val id: UUID,
    val type: IssueHistoryEventTypeDto,
    val performedBy: AccountDto,
    val createdAt: OffsetDateTime,
    val statusFrom: IssueStatusDto?,
    val statusTo: IssueStatusDto?,
    val commentText: String?,
)