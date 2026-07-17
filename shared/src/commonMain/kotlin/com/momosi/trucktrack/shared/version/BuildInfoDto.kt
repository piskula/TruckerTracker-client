package com.momosi.trucktrack.shared.version

import kotlin.time.Instant
import kotlinx.serialization.Serializable

@Serializable
data class BuildInfoDto(
    val version: String,
    val time: Instant,
    val name: String,
)
