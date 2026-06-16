package com.momosi.trucktrack.core.issue.model

enum class IssueStatus {
    Open,
    InProgress,
    Done,
    ;

    companion object {
        fun fromApiValue(value: String): IssueStatus = when (value) {
            "OPEN" -> Open
            "IN_PROGRESS" -> InProgress
            "DONE" -> Done
            else -> Open
        }
    }

    fun toApiValue(): String = when (this) {
        Open -> "OPEN"
        InProgress -> "IN_PROGRESS"
        Done -> "DONE"
    }
}

