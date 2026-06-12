package sk.momosilabs.truckTrack.issue.service.getIssue

import sk.momosilabs.truckTrack.issue.model.IssueModel

interface GetIssueUseCase {

    fun get(issueId: Long): IssueModel
}
