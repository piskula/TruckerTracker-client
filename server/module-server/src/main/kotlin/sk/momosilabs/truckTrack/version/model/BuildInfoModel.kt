package sk.momosilabs.truckTrack.version.model

import java.time.OffsetDateTime

data class BuildInfoModel(
    val version: String,
    val time: OffsetDateTime,
    val name: String,
)
