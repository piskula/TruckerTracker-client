package com.momosi.trucktrack.shared.issue

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.time.Instant
import kotlinx.serialization.Serializable

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class IssueHistoryDto(
    val id: Uuid,
    val type: IssueHistoryEventTypeDto,
    val performedBy: AccountDto,
    val createdAt: Instant,
    val statusFrom: IssueStatusDto?,
    val statusTo: IssueStatusDto?,
    val commentText: String?,
)
