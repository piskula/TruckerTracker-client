package com.momosi.trucktrack.feature.issues.impl.list

sealed interface IssuesAction {
    data class SelectFilter(val filter: IssueFilter) : IssuesAction
    data class OpenIssue(val issueId: Long) : IssuesAction
    data object CreateIssue : IssuesAction
    data object Retry : IssuesAction
    data object Refresh : IssuesAction
}

sealed interface IssueFilter {

    enum class Driver : IssueFilter {
        MyOpen,
        MyClosed,
        All,
    }

    enum class Mechanic : IssueFilter {
        MyIssues,
        Open,
        All,
    }
}
