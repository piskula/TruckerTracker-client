package com.momosi.trucktrack.shared.issue

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalUuidApi::class, ExperimentalSerializationApi::class)
@Serializable
data class IssueFilterDto(
    @EncodeDefault val statuses: List<IssueStatusDto> = emptyList(),
    @EncodeDefault val vehicleIds: List<Long> = emptyList(),
    @EncodeDefault val accountIds: List<Uuid> = emptyList(),
)
