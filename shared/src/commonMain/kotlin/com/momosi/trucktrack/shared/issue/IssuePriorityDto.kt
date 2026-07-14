package com.momosi.trucktrack.shared.issue

import kotlinx.serialization.Serializable

@Serializable
enum class IssuePriorityDto {
    P1_HIGH,
    P3_MEDIUM,
    P5_LOW,
}
