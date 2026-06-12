package sk.momosilabs.truckTrack.issue.service

import sk.momosilabs.truckTrack.issue.entity.IssuePriority
import sk.momosilabs.truckTrack.issue.entity.IssueStatus

data class IssueListFilter(
    val status: IssueStatus? = null,
    val priority: IssuePriority? = null,
    val search: String? = null,
    val vehicleId: Long? = null,
)
