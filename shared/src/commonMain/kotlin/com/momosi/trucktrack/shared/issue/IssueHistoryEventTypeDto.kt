package com.momosi.trucktrack.shared.issue

import kotlinx.serialization.Serializable

@Serializable
enum class IssueHistoryEventTypeDto {
    STATUS_CHANGE, ASSIGNEE_CHANGE, COMMENT
}
