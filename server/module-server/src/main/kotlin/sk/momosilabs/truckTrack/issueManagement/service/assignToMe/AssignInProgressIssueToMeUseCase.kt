package sk.momosilabs.truckTrack.issueManagement.service.assignToMe

import sk.momosilabs.truckTrack.issueManagement.model.IssueModel

interface AssignInProgressIssueToMeUseCase {

    fun reassign(issueId: Long): IssueModel

}
