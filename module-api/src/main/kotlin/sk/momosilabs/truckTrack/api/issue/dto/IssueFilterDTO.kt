package sk.momosilabs.truckTrack.api.issue.dto

import java.util.UUID

data class IssueFilterDTO(
    val statuses: List<IssueStatusDTO> = emptyList(),
    val vehicleIds: List<Long> = emptyList(),
    val accountIds: List<UUID> = emptyList(),
)
