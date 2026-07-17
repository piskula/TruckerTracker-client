package sk.momosilabs.truckTrack.api.common

import java.util.UUID

data class ErrorDto(
    val userMessage: String,
    val errorIdentifier: UUID,
)
