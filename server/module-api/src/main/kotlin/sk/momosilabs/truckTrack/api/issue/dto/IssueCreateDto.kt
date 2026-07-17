package sk.momosilabs.truckTrack.api.issue.dto

data class IssueCreateDto(
    val vehicleId: Long,
    val title: String,
    val description: String,
    val priority: IssuePriorityDto,
)
