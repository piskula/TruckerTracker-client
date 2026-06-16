package com.momosi.trucktrack.feature.issues.impl.list

import com.momosi.trucktrack.core.issue.model.IssueStatus

sealed interface IssuesAction {
    data class SelectFilter(val filter: StatusFilter) : IssuesAction
    data class OpenIssue(val issueId: Long) : IssuesAction
    data object CreateIssue : IssuesAction
    data object Retry : IssuesAction
}

enum class StatusFilter(val status: IssueStatus?) {
    All(status = null),
    Open(status = IssueStatus.Open),
    InProgress(status = IssueStatus.InProgress),
    Done(status = IssueStatus.Done),
}
