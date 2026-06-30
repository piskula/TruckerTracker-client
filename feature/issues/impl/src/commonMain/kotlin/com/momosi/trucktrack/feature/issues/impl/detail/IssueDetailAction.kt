package com.momosi.trucktrack.feature.issues.impl.detail

sealed interface IssueDetailAction {
    data class UpdateComment(val text: String) : IssueDetailAction
    data class UploadPhoto(val uri: String) : IssueDetailAction
    data object SendComment : IssueDetailAction
    data object Retry : IssueDetailAction
    data object StartWorking : IssueDetailAction
    data object ResolveIssue : IssueDetailAction
    data object ReassignToMe : IssueDetailAction
}
