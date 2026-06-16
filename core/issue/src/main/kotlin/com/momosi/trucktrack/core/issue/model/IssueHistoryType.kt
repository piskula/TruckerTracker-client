package com.momosi.trucktrack.core.issue.model

enum class IssueHistoryType {
    StatusChange,
    Comment,
    ;

    companion object {
        fun fromApiValue(value: String): IssueHistoryType = when (value) {
            "STATUS_CHANGE" -> StatusChange
            "COMMENT" -> Comment
            else -> Comment
        }
    }
}

