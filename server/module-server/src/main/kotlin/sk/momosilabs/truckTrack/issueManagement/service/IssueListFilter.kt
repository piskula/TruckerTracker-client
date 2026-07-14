package sk.momosilabs.truckTrack.issueManagement.service

import sk.momosilabs.truckTrack.issueManagement.entity.IssueStatus
import java.util.UUID

data class IssueListFilter(
    val statuses: List<IssueStatus>,
    val vehicleIds: List<Long>,
    val accountIds: List<UUID>,
) {
    companion object {
        val emptyFilter = IssueListFilter(
            statuses = emptyList(),
            vehicleIds = emptyList(),
            accountIds = emptyList(),
        )
    }
}
