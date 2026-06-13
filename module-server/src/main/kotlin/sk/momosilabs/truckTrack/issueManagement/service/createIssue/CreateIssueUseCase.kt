package sk.momosilabs.truckTrack.issueManagement.service.createIssue

import sk.momosilabs.truckTrack.issueManagement.entity.IssuePriority
import sk.momosilabs.truckTrack.issueManagement.model.IssueModel

data class CreateIssueCommand(
    val vehicleId: Long,
    val title: String,
    val description: String,
    val priority: IssuePriority,
)

interface CreateIssueUseCase {

    fun create(command: CreateIssueCommand): IssueModel
}
