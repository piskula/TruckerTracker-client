package sk.momosilabs.truckTrack.issueManagement.service.getIssue

import sk.momosilabs.truckTrack.issueManagement.model.IssueModel

interface GetIssueUseCase {

    fun get(issueId: Long): IssueModel
}
