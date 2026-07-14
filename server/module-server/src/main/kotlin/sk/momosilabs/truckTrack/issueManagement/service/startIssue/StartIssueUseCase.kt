package sk.momosilabs.truckTrack.issueManagement.service.startIssue

import sk.momosilabs.truckTrack.issueManagement.model.IssueModel

interface StartIssueUseCase {

    fun start(issueId: Long): IssueModel
}
