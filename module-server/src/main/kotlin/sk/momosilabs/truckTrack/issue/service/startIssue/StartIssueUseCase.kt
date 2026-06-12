package sk.momosilabs.truckTrack.issue.service.startIssue

import sk.momosilabs.truckTrack.issue.model.IssueModel

interface StartIssueUseCase {

    fun start(issueId: Long): IssueModel
}
