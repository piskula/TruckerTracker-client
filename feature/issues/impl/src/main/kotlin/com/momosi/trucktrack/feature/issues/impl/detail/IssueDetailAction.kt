package com.momosi.trucktrack.feature.issues.impl.detail

sealed interface IssueDetailAction {
    data class UpdateComment(val text: String) : IssueDetailAction
    data object SendComment : IssueDetailAction
    data object Retry : IssueDetailAction
}

