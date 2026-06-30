package com.momosi.trucktrack.core.issue.model

import com.momosi.trucktrack.core.vehicle.model.Vehicle
import java.time.Instant

data class Issue(
    val id: Long,
    val title: String,
    val description: String,
    val status: IssueStatus,
    val priority: IssuePriority,
    val vehicle: Vehicle?,
    val reportedBy: Account?,
    val assignedTo: Account?,
    val createdAt: Instant,
    val updatedAt: Instant,
)
