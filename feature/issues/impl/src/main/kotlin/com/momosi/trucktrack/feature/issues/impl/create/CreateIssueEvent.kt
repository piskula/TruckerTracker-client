package com.momosi.trucktrack.feature.issues.impl.create

sealed interface CreateIssueEvent {
    data class IssueCreated(val issueId: Long) : CreateIssueEvent
    data object CreationFailed : CreateIssueEvent
}

