package sk.momosilabs.truckTrack.api.issue.dto

import java.util.UUID

data class IssueFilterDto(
    val statuses: List<IssueStatusDto> = emptyList(),
    val vehicleIds: List<Long> = emptyList(),
    val accountIds: List<UUID> = emptyList(),
)