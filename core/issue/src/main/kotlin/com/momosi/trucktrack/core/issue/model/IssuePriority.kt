package com.momosi.trucktrack.core.issue.model

enum class IssuePriority {
    High,
    Medium,
    Low,
    ;

    companion object {
        fun fromApiValue(value: String): IssuePriority = when (value) {
            "P1_HIGH" -> High
            "P3_MEDIUM" -> Medium
            "P5_LOW" -> Low
            else -> Medium
        }
    }

    fun toApiValue(): String = when (this) {
        High -> "P1_HIGH"
        Medium -> "P3_MEDIUM"
        Low -> "P5_LOW"
    }
}

