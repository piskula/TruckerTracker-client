package sk.momosilabs.truckTrack.account.model

import java.util.UUID

data class AccountModel(
    val id: UUID,
    val username: String,
    val firstName: String,
    val lastName: String,
)
