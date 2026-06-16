package com.momosi.trucktrack.core.issue.dto

import com.momosi.trucktrack.core.vehicle.dto.VehicleDto
import kotlinx.serialization.Serializable

@Serializable
data class IssueDto(
    val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val status: String = "",
    val priority: String = "",
    val vehicle: VehicleDto? = null,
    val reportedBy: AccountDto? = null,
    val assignedTo: AccountDto? = null,
    val createdAt: String = "",
    val updatedAt: String = "",
)

