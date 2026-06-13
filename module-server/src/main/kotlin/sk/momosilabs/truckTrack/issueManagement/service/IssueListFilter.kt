package sk.momosilabs.truckTrack.issueManagement.service

import sk.momosilabs.truckTrack.issueManagement.entity.IssuePriority
import sk.momosilabs.truckTrack.issueManagement.entity.IssueStatus

data class IssueListFilter(
    val status: IssueStatus? = null,
    val priority: IssuePriority? = null,
    val search: String? = null,
    val vehicleId: Long? = null,
)
