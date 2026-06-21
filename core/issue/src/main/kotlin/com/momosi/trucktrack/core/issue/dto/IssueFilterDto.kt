package com.momosi.trucktrack.core.issue.dto

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
data class IssueFilterDto(@EncodeDefault val statuses: List<String> = emptyList(), @EncodeDefault val vehicleIds: List<Long> = emptyList(), @EncodeDefault val accountIds: List<String> = emptyList())
