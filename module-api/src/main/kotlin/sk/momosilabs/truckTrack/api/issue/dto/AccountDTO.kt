package sk.momosilabs.truckTrack.api.issue.dto

import java.util.UUID

data class AccountDTO(
    val id: UUID,
    val username: String,
    val firstName: String,
    val lastName: String,
)
