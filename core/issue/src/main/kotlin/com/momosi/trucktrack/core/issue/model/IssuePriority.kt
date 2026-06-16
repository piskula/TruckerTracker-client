package com.momosi.trucktrack.core.issue.model

enum class IssuePriority {
    High,
    Medium,
    Low,
    ;

    companion object {
        fun fromApiValue(value: String): IssuePriority = when (value) {
            "HIGH" -> High
            "MEDIUM" -> Medium
            "LOW" -> Low
            else -> Medium
        }
    }

    fun toApiValue(): String = when (this) {
        High -> "HIGH"
        Medium -> "MEDIUM"
        Low -> "LOW"
    }
}

