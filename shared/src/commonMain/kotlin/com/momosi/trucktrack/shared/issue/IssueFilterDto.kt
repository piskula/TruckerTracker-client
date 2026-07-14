package com.momosi.trucktrack.shared.issue

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class IssueFilterDto(
    val statuses: List<IssueStatusDto> = emptyList(),
    val vehicleIds: List<Long> = emptyList(),
    val accountIds: List<Uuid> = emptyList(),
)
