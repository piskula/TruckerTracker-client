package com.momosi.trucktrack.core.issue.dto

import kotlinx.serialization.Serializable

@Serializable
data class IssueCreateDto(
    val vehicleId: Long,
    val title: String,
    val description: String,
    val priority: String,
)

