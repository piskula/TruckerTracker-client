package sk.momosilabs.truckTrack.issue.service.createIssue

import sk.momosilabs.truckTrack.issue.entity.IssuePriority
import sk.momosilabs.truckTrack.issue.model.IssueModel

data class CreateIssueCommand(
    val vehicleId: Long,
    val title: String,
    val description: String,
    val priority: IssuePriority,
)

interface CreateIssueUseCase {

    fun create(command: CreateIssueCommand): IssueModel
}
