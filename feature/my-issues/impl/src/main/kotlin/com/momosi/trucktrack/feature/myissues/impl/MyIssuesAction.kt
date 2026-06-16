package com.momosi.trucktrack.feature.myissues.impl

import com.momosi.trucktrack.core.issue.model.IssueStatus

sealed interface MyIssuesAction {
    data class SelectFilter(val filter: StatusFilter) : MyIssuesAction
    data class OpenIssue(val issueId: Long) : MyIssuesAction
    data object CreateIssue : MyIssuesAction
    data object Retry : MyIssuesAction
}

enum class StatusFilter(val status: IssueStatus?) {
    All(status = null),
    Open(status = IssueStatus.Open),
    InProgress(status = IssueStatus.InProgress),
    Done(status = IssueStatus.Done),
}

