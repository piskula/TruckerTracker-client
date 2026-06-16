package com.momosi.trucktrack.core.issue.model

data class IssueCreate(
    val vehicleId: Long,
    val title: String,
    val description: String,
    val priority: IssuePriority,
)

