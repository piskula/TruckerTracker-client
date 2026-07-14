package sk.momosilabs.truckTrack.issueManagement.service.resolveIssue

import sk.momosilabs.truckTrack.issueManagement.model.IssueModel

interface ResolveIssueUseCase {

    fun resolve(issueId: Long): IssueModel
}
