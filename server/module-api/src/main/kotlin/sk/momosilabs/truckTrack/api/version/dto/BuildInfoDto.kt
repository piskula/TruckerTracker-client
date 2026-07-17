package sk.momosilabs.truckTrack.api.version.dto

import java.time.OffsetDateTime

data class BuildInfoDto(
    val version: String,
    val time: OffsetDateTime,
    val name: String,
)
