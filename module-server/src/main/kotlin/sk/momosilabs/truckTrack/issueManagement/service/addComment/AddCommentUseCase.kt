package sk.momosilabs.truckTrack.issueManagement.service.addComment

import sk.momosilabs.truckTrack.issueManagement.model.IssueHistoryModel

data class AddCommentCommand(
    val issueId: Long,
    val text: String,
)

interface AddCommentUseCase {
    fun addComment(command: AddCommentCommand): IssueHistoryModel
}
