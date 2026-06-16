package com.momosi.trucktrack.core.issue.model

import java.time.Instant

data class IssueHistory(
    val id: String,
    val type: IssueHistoryType,
    val performedBy: Account?,
    val createdAt: Instant,
    val statusFrom: IssueStatus?,
    val statusTo: IssueStatus?,
    val commentText: String?,
)

