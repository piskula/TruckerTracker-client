package sk.momosilabs.truckTrack.issue.service.resolveIssue

import sk.momosilabs.truckTrack.issue.model.IssueModel

interface ResolveIssueUseCase {

    fun resolve(issueId: Long): IssueModel
}
