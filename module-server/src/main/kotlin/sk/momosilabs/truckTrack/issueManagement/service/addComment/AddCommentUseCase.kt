package sk.momosilabs.truckTrack.issueManagement.service.addComment

import sk.momosilabs.truckTrack.issueManagement.model.IssueHistoryModel

interface AddCommentUseCase {
    fun addComment(issueId: Long, comment: String): IssueHistoryModel
}
