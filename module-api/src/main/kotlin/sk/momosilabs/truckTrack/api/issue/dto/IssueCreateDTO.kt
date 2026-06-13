package sk.momosilabs.truckTrack.api.issue.dto

data class IssueCreateDTO(
    val vehicleId: Long,
    val title: String,
    val description: String,
    val priority: IssuePriorityDTO,
)
